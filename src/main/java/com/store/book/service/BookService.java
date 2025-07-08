package com.store.book.service;

import com.store.book.dao.BookDAO;
import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Publisher;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        BookDtoResponse bookDtoResponse = bookMapper.entityToDto(book);
        bookDtoResponse.setAuthorName(author.getFirstName() + " " + author.getLastName());
        bookDtoResponse.setPublisherName(publisher.getName());
        return bookDtoResponse;
    }

    public BookDtoResponse getBookById(Long id) {
        Book book = bookDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id: " + id + " not found"));
        BookDtoResponse bookDtoResponse = bookMapper.entityToDto(book);
        bookDtoResponse.setAuthorName(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        bookDtoResponse.setPublisherName(book.getPublisher().getName());
        return bookDtoResponse;
    }

    public Book getBookWithDetailsById(Long id) {
        return bookDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id: " + id + " not found"));
    }
}
