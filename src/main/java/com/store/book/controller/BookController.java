package com.store.book.controller;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookDtoResponse> createBook(@Valid BookDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(@RequestParam Genre genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookDtoResponse>> getBooksByAuthor(@RequestParam Long id) {
        return ResponseEntity.ok(bookService.getBooksByAuthorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-books")
    public ResponseEntity<List<BookDtoResponse>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<List<BookDtoResponse>> getMostViewed() {
        return ResponseEntity.ok(bookService.get10MostViewedBooksForToday());
    }

    @PostMapping("/favorite-book/{id}")
    public ResponseEntity<List<BookDtoResponse>> addFavoriteBook(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addFavoriteBook(id));
    }

    @GetMapping("/favorite-books-list")
    public ResponseEntity<List<BookDtoResponse>> getFavoriteBooksList() {
        return ResponseEntity.ok(bookService.getFavoriteBooks());
    }

    @GetMapping("/all-books-with-pages")
    public ResponseEntity<Page<BookDtoResponse>> getAllBooksWithPages(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }
}
