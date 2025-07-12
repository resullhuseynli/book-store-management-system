package com.store.book.controller;

import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.service.impl.DiscountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
@Validated
public class DiscountController {

    private final DiscountServiceImpl discountService;

    @PostMapping
    public ResponseEntity<DiscountDtoResponse> createDiscount(@Valid @RequestBody DiscountDtoRequest request) {
        return ResponseEntity.ok(discountService.create(request));
    }

    @GetMapping("/active-discounts")
    public ResponseEntity<List<DiscountDtoResponse>> getAllActiveDiscounts() {
        return ResponseEntity.ok(discountService.getAllActiveDiscounts());
    }

    @GetMapping("/all-discounts")
    public ResponseEntity<List<DiscountDtoResponse>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
