package com.store.book.controller;

import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.dao.model.BillModel;
import com.store.book.service.BillGeneratorService;
import com.store.book.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final BillGeneratorService billGeneratorService;

    @GetMapping("/by-user")
    public ResponseEntity<CartDtoResponse> get() {
        return ResponseEntity.ok(cartService.get());
    }

    @PostMapping
    public ResponseEntity<CartDtoResponse> buy() {
        return ResponseEntity.ok(cartService.buy());
    }

    @PostMapping("/bill")
    public ResponseEntity<byte[]> getBill(@RequestBody BillModel billModel) {
        byte[] pdf = billGeneratorService.generateBillPdf(billModel);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
