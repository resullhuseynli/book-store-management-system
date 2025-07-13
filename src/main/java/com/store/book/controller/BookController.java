package com.store.book.controller;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.enums.Genre;
import com.store.book.service.impl.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

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
        return ResponseEntity.ok(bookService.get10MostViewedBooks());
    }
}
