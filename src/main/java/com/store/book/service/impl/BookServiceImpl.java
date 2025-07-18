package com.store.book.service.impl;

import com.store.book.dao.BookRepository;
import com.store.book.dao.UserEntityRepository;
import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Publisher;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Genre;
import com.store.book.exception.exceptions.DataIsAlreadyAddedException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.BookService;
import com.store.book.service.ViewTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id: " + id + " not found"));
    }

    @Override
    public List<BookDtoResponse> getBooksByGenre(Genre genre) {
        return bookRepository.getBooksByGenre(genre).stream()
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Book book = getBookWithDetailsById(id);
        bookRepository.delete(book);
    }

    @Override
    public List<BookDtoResponse> getBooksByAuthorId(Long authorId) {
        Author author = authorService.getById(authorId);
        return bookRepository.getBooksByAuthor(author).stream()
                .map(bookMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookDtoResponse> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDtoResponse> get10MostViewedBooksForToday() {
        Set<String> viewedBooks = viewTrackerService.getTop10BookIdsForToday();
        List<BookDtoResponse> response = new ArrayList<>();
        for (String bookId : viewedBooks) {
            BookDtoResponse book = bookMapper.entityToDto(getBookWithDetailsById(Long.parseLong(bookId)));
            response.add(book);
        }
        return response;
    }

    @Override
    public List<BookDtoResponse> addFavoriteBook(Long bookId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        List<Book> bookList = user.getFavoriteBooks();
        if (bookList == null) {
            bookList = new ArrayList<>();
        }
        Book book = getBookWithDetailsById(bookId);
        if (bookList.contains(book)) {
            throw new DataIsAlreadyAddedException("Book was already favoured");
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
        List<BookDtoResponse> response = bookMapper.entityToDtoList(bookPage.getContent());
        return new PageImpl<>(response);
    }

    @Override
    public List<BookDtoResponse> getTop10BooksLast7Days() {
        Set<String> booksIdsForLastWeek = viewTrackerService.getTop10BooksLast7Days();
        List<BookDtoResponse> allBooks = new ArrayList<>();
        for (String bookId : booksIdsForLastWeek) {
            BookDtoResponse book = bookMapper.entityToDto(getBookWithDetailsById(Long.parseLong(bookId)));
            allBooks.add(book);
        }
        return allBooks;
    }
}
