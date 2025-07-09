package com.store.book.controller;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enums")
public class EnumsController {

    @GetMapping("/get-all-genres")
    public ResponseEntity<List<Genre>>  getAllGenres() {
        return ResponseEntity.ok().body(Arrays.asList(Genre.values()));
    }

    @GetMapping("/get-all-languages")
    public ResponseEntity<List<Language>> getAllLanguages() {
        return ResponseEntity.ok().body(Arrays.asList(Language.values()));
    }

}
