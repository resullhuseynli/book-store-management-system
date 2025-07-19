package com.store.book.service;

import com.store.book.dao.CartRepository;
import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.UserEntity;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.CartMapper;
import com.store.book.mapper.ItemMapper;
import com.store.book.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemMapper itemMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final CartMapper cartMapper;

    public CartDtoResponse get() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException("Cart not found"));
        return cartMapper.entityToDto(cart);
    }

}
