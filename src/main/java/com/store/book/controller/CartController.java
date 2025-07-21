package com.store.book.controller;

import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/by-user")
    public ResponseEntity<CartDtoResponse> get() {
        return ResponseEntity.ok(cartService.get());
    }

    @PostMapping
    public ResponseEntity<CartDtoResponse> buy() {
        return ResponseEntity.ok(cartService.buy());
    }

}
