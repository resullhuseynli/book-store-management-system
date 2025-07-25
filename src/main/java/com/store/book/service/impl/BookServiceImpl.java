package com.store.book.service.impl;

import com.store.book.dao.BookRepository;
import com.store.book.dao.UserEntityRepository;
import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.dto.QuantityDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Publisher;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Genre;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.BookService;
import com.store.book.service.ViewTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorServiceImpl authorService;
    private final PublisherServiceImpl publisherService;
    private final ViewTrackerService viewTrackerService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserEntityRepository userEntityRepository;
    private final MessageSource messageSource;

    @Override
    public BookDtoResponse create(BookDtoRequest request) {
        Book book = bookMapper.dtoToEntity(request);
        Author author = authorService.getById(request.getAuthorId());
        Publisher publisher = publisherService.getById(request.getPublisherId());
        book.setAuthor(author);
        book.setPublisher(publisher);
        bookRepository.save(book);
        return bookMapper.entityToDto(book);
    }

    @Override
    public BookDtoResponse getById(Long id) {
        Book book = getBookWithDetailsById(id);
        viewTrackerService.bookTrackView(id);
        return bookMapper.entityToDto(book);
    }

    @Override
    public Book getBookWithDetailsById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("BookNotFound", null, locale)));
    }

    @Override
    public List<BookDtoResponse> getBooksByGenre(Genre genre) {
        return bookRepository.getBooksByGenre(genre).stream()
                .filter(b -> b.getStatus().equals(Status.ACTIVE))
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Book book = getBookWithDetailsById(id);
        book.setStatus(Status.DELETED);
        bookRepository.save(book);
    }

    @Override
    public List<BookDtoResponse> getBooksByAuthorId(Long authorId) {
        Author author = authorService.getById(authorId);
        return bookRepository.getBooksByAuthor(author).stream()
                .filter(b -> b.getStatus().equals(Status.ACTIVE))
                .map(bookMapper::entityToDto)
                .toList();
    }

    @Override
    public List<BookDtoResponse> getAll() {
        return bookRepository.findAll().stream()
                .filter(b -> b.getStatus().equals(Status.ACTIVE))
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDtoResponse> get10MostViewedBooksForToday() {
        Set<String> viewedBooks = viewTrackerService.getTop10BookIdsForToday();
        List<BookDtoResponse> response = new ArrayList<>();
        for (String bookId : viewedBooks) {
            try {
                BookDtoResponse book = bookMapper.entityToDto(getBookWithDetailsById(Long.parseLong(bookId)));
                response.add(book);
            } catch (NotFoundException ignored) {
            }
        }
        return response;
    }

    @Override
    public List<BookDtoResponse> addFavoriteBook(Long bookId) {
        final Locale locale = LocaleContextHolder.getLocale();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        List<Book> bookList = user.getFavoriteBooks();
        if (bookList == null) {
            bookList = new ArrayList<>();
        }
        Book book = getBookWithDetailsById(bookId);
        if (bookList.contains(book)) {
            throw new EntityContainException(
                    messageSource.getMessage("BookContainsMessage", null, locale));
        }
        bookList.add(book);
        userEntityRepository.save(user);
        return bookMapper.entityToDtoList(bookList);
    }

    @Override
    public List<BookDtoResponse> getFavoriteBooks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        List<Book> bookList = user.getFavoriteBooks();
        return bookMapper.entityToDtoList(bookList);
    }

    @Override
    public Page<BookDtoResponse> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<Book> bookList = bookPage.getContent().stream()
                .filter(b -> b.getStatus().equals(Status.ACTIVE))
                .toList();
        List<BookDtoResponse> response = bookMapper.entityToDtoList(bookList);
        return new PageImpl<>(response, pageable, response.size());
    }

    @Override
    public List<BookDtoResponse> getTop10BooksLast7Days() {
        Set<String> booksIdsForLastWeek = viewTrackerService.getTop10BooksLast7Days();
        List<BookDtoResponse> allBooks = new ArrayList<>();
        for (String bookId : booksIdsForLastWeek) {
            try {
                BookDtoResponse book = bookMapper.entityToDto(getBookWithDetailsById(Long.parseLong(bookId)));
                allBooks.add(book);
            } catch (NotFoundException ignored) {
            }
        }
        return allBooks;
    }

    @Override
    public List<BookDtoResponse> get10BooksWithMostRating() {
        return getAll().stream()
                .sorted(Comparator.comparing(BookDtoResponse::getRating).reversed())
                .limit(10)
                .toList();
    }

    @Override
    public void updateQuantity(QuantityDtoRequest request) {
        Book book = getBookWithDetailsById(request.getBookId());
        book.setAmount(request.getQuantity() + book.getAmount());
        bookRepository.save(book);
    }
}
