package com.store.book.service;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.dto.QuantityDtoRequest;
import com.store.book.dao.entity.Book;
import com.store.book.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService extends BaseService<BookDtoRequest, BookDtoResponse> {

    Book getBookWithDetailsById(Long id);

    List<BookDtoResponse> getBooksByGenre(Genre genre);

    List<BookDtoResponse> getBooksByAuthorId(Long authorId);

    List<BookDtoResponse> get10MostViewedBooksForToday();

    List<BookDtoResponse> addFavoriteBook(Long bookId);

    List<BookDtoResponse> getFavoriteBooks();

    Page<BookDtoResponse> getAllBooks(Pageable pageable);

    List<BookDtoResponse> getTop10BooksLast7Days();

    List<BookDtoResponse> get10BooksWithMostRating();

    void updateQuantity(QuantityDtoRequest request);
}
