package com.store.book.controller;

import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok().body(authorService.getAllAuthors());
    }

    @PostMapping
    public ResponseEntity<Author> saveAuthor(@RequestBody AuthorDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody AuthorDtoRequest request, @PathVariable Long id) {
        return ResponseEntity.ok().body(authorService.updateAuthor(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok().body(authorService.getAuthorById(id));
    }
}
