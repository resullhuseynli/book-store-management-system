package com.store.book.controller;

import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return ResponseEntity.ok().body(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok().body(publisherService.getPublisherById(id));
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@RequestBody PublisherDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@RequestBody PublisherDtoRequest request, @PathVariable Long id) {
        return ResponseEntity.ok().body(publisherService.updatePublisher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
