package com.store.book.controller;

import com.store.book.dao.dto.CommentDtoRequest;
import com.store.book.dao.dto.CommentDtoResponse;
import com.store.book.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDtoResponse> create(@Valid @RequestBody CommentDtoRequest commentDtoRequest) {
        return ResponseEntity.ok(commentService.create(commentDtoRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDtoResponse>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<CommentDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @GetMapping("/public/book")
    public ResponseEntity<List<CommentDtoResponse>> getAllByBookId(@RequestParam Long book) {
        return ResponseEntity.ok(commentService.getCommentsByBookId(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
