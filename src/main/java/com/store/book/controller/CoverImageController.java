package com.store.book.controller;

import com.store.book.service.CoverImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cover-image")
public class CoverImageController {

    private final CoverImageService coverImageService;

    @PostMapping(path = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCoverImage(@PathVariable Long id, @RequestPart("file") MultipartFile coverImage) throws IOException {

        if (coverImage == null || coverImage.isEmpty()) return ResponseEntity.badRequest().body("File is empty.");

        String fileName = coverImage.getOriginalFilename();
        String contentType = coverImage.getContentType();

        if (fileName == null) return ResponseEntity.badRequest().body("File is empty.");

        List<String> allowedExtensions = List.of(".png", ".jpg", ".jpeg");

        boolean validExtension = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith(ext));

        if (contentType == null || !contentType.startsWith("image/") ||
                !validExtension) {

            return ResponseEntity.badRequest().body("Only images are allowed.");
        }
        try {
            coverImageService.uploadCoverImage(id, coverImage);
        } catch (IOException ioException) {
            throw new IOException("Something went wrong while uploading cover image");
        }
        return ResponseEntity.ok().body("Image uploaded successfully.");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getCoverImageById(@PathVariable Long id) throws IOException {
        try {
            return coverImageService.getCoverImage(id);
        } catch (IOException ioException) {
            throw new IOException("Something went wrong while reading cover image");
        }
    }
}
