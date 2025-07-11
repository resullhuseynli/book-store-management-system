package com.store.book.service;

import com.store.book.dao.BookDAO;
import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Publisher;
import com.store.book.enums.Genre;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookDAO bookDAO;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    public BookDtoResponse createBook(BookDtoRequest request) {
        Book book = bookMapper.dtoToEntity(request);
        Author author = authorService.getAuthorById(request.getAuthorId());
        Publisher publisher = publisherService.getPublisherById(request.getPublisherId());
        book.setAuthor(author);
        book.setPublisher(publisher);
        bookDAO.save(book);
        return bookMapper.entityToDto(book);
    }

    public BookDtoResponse getBookById(Long id) {
        Book book = getBookWithDetailsById(id);
        return bookMapper.entityToDto(book);
    }

    public Book getBookWithDetailsById(Long id) {
        return bookDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id: " + id + " not found"));
    }

    public List<BookDtoResponse> getBooksByGenre(Genre genre) {
        return bookDAO.getBooksByGenre(genre).stream()
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public void deleteBookById(Long id) {
        Book book = getBookWithDetailsById(id);
        bookDAO.delete(book);
    }

    public List<BookDtoResponse> getBooksByAuthorId(Long authorId) {
        Author author = authorService.getAuthorById(authorId);
        return bookDAO.getBooksByAuthor(author).stream()
                .map(bookMapper::entityToDto).collect(Collectors.toList());
    }
}
