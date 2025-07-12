package com.store.book.service.impl;

import com.store.book.dao.BookDAO;
import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Publisher;
import com.store.book.enums.Genre;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.BookMapper;
import com.store.book.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BaseService<BookDtoRequest, BookDtoResponse> {

    private final BookDAO bookDAO;
    private final BookMapper bookMapper;
    private final AuthorServiceImpl authorService;
    private final PublisherServiceImpl publisherService;

    @Override
    public BookDtoResponse create(BookDtoRequest request) {
        Book book = bookMapper.dtoToEntity(request);
        Author author = authorService.getById(request.getAuthorId());
        Publisher publisher = publisherService.getById(request.getPublisherId());
        book.setAuthor(author);
        book.setPublisher(publisher);
        bookDAO.save(book);
        return bookMapper.entityToDto(book);
    }

    @Override
    public BookDtoResponse getById(Long id) {
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

    @Override
    public void deleteById(Long id) {
        Book book = getBookWithDetailsById(id);
        bookDAO.delete(book);
    }

    public List<BookDtoResponse> getBooksByAuthorId(Long authorId) {
        Author author = authorService.getById(authorId);
        return bookDAO.getBooksByAuthor(author).stream()
                .map(bookMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookDtoResponse> getAll() {
        return bookDAO.findAll().stream()
                .map(bookMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
