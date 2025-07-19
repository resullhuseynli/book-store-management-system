package com.store.book.service.impl;

import com.store.book.dao.CartRepository;
import com.store.book.dao.ItemRepository;
import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.entity.UserEntity;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.ItemMapper;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final CartRepository cartRepository;

    @Override
    public ItemDtoResponse getById(Long id) {
        Item item = getItemWithDetails(id);
        return itemMapper.entityToDto(item);
    }

    public Item getItemWithDetails(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public List<ItemDtoResponse> getAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoResponse> getAllItemsByCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException("Cart not found"));
        return itemRepository.findAll().stream()
                .filter(cartBook -> cartBook.getCart().equals(cart))
                .map(itemMapper::entityToDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Item item = getItemWithDetails(id);
        itemRepository.delete(item);
    }

    @Override
    public ItemDtoResponse create(ItemDtoRequest entity) {
        Item item = itemMapper.dtoToEntity(entity);
        Item saved = itemRepository.save(item);
        return itemMapper.entityToDto(saved);
    }
}
