package com.store.book.controller;

import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoRequestAll;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
@Validated
public class DiscountController {

    private final DiscountService discountService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DiscountDtoResponse> createDiscount(@Valid @RequestBody DiscountDtoRequest request) {
        return ResponseEntity.ok(discountService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public ResponseEntity<String> addAllDiscount(@Valid @RequestBody DiscountDtoRequestAll request) {
        discountService.addAll(request);
        return ResponseEntity.ok("Discount added successfully");
    }

    @GetMapping("/public/active")
    public ResponseEntity<List<DiscountDtoResponse>> getAllActiveDiscounts() {
        return ResponseEntity.ok(discountService.getAllActiveDiscounts());
    }

    @GetMapping("/all")
    public ResponseEntity<List<DiscountDtoResponse>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
