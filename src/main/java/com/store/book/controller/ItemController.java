package com.store.book.controller;

import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDtoResponse> addItem(@RequestBody ItemDtoRequest request){
        return ResponseEntity.ok(itemService.create(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<List<ItemDtoResponse>> getAllItems(){
        return ResponseEntity.ok(itemService.getAll());
    }
}
