package com.store.book.controller;

import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody AuthorDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
    }

    @GetMapping("/all-authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok().body(authorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok().body(authorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthorsByName(@RequestParam String name) {
        return ResponseEntity.ok().body(authorService.getAuthorsByName(name));
    }

    @GetMapping("/all-authors-with-page")
    public ResponseEntity<Page<Author>> getAllAuthorsWithPage(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody AuthorDtoRequest request, @PathVariable Long id) {
        return ResponseEntity.ok().body(authorService.updateAuthor(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
