package com.store.book.service;

import com.store.book.dao.BookDAO;
import com.store.book.dao.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class CoverImageService {

    private final Path uploadDirectory = Paths.get("uploads");
    private final BookDAO bookDAO;
    private final BookService bookService;

    public CoverImageService(BookDAO bookDAO, BookService bookService) throws IOException {
        this.bookService = bookService;
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }
        this.bookDAO = bookDAO;
    }

    @Transactional
    public void uploadCoverImage(Long bookId, MultipartFile coverImage) throws IOException {
        Book book = bookService.getBookWithDetailsById(bookId);
        String imageName = UUID.randomUUID() + "-" + coverImage.getOriginalFilename();
        Path filePath = uploadDirectory.resolve(imageName);
        Files.copy(coverImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        book.setCoverImageUrl(filePath.toAbsolutePath().toString());
        bookDAO.save(book);
    }

    public ResponseEntity<byte[]> getCoverImage(Long bookId) throws IOException {
        Book book = bookService.getBookWithDetailsById(bookId);
        Path imagePath = Path.of(book.getCoverImageUrl());
        if (!Files.exists(imagePath)) {
            return ResponseEntity.noContent().build();
        }

        String contentType = Files.probeContentType(imagePath);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }

}
