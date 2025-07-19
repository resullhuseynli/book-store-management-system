package com.store.book.controller;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.dto.QuantityDtoRequest;
import com.store.book.enums.Genre;
import com.store.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookDtoResponse> createBook(@Valid BookDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @PostMapping("/favorite-book/{id}")
    public ResponseEntity<List<BookDtoResponse>> addFavoriteBook(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addFavoriteBook(id));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/public/genre")
    public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(@RequestParam Genre genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    @GetMapping("/public/author")
    public ResponseEntity<List<BookDtoResponse>> getBooksByAuthor(@RequestParam Long id) {
        return ResponseEntity.ok(bookService.getBooksByAuthorId(id));
    }

    @GetMapping("/all-books")
    public ResponseEntity<List<BookDtoResponse>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/public/most-viewed")
    public ResponseEntity<List<BookDtoResponse>> getMostViewed() {
        return ResponseEntity.ok(bookService.get10MostViewedBooksForToday());
    }

    @GetMapping("/public/favorite-books-list")
    public ResponseEntity<List<BookDtoResponse>> getFavoriteBooksList() {
        return ResponseEntity.ok(bookService.getFavoriteBooks());
    }

    @GetMapping("/public/all-books-with-pages")
    public ResponseEntity<Page<BookDtoResponse>> getAllBooksWithPages(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/public/last-7-days-most-viewed")
    public ResponseEntity<List<BookDtoResponse>> getLast7DaysMostViewed() {
        return ResponseEntity.ok(bookService.getTop10BooksLast7Days());
    }

    @GetMapping("/public/most-rating")
    public ResponseEntity<List<BookDtoResponse>> get10MostRatingBooks() {
        return ResponseEntity.ok(bookService.get10BooksWithMostRating());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/quantity")
    public ResponseEntity<Void> updateQuantity(@Valid @RequestBody QuantityDtoRequest request) {
        bookService.updateQuantity(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
