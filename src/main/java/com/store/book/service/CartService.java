package com.store.book.service;

import com.store.book.dao.CartRepository;
import com.store.book.dao.UserEntityRepository;
import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.NotEnoughMoneyException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.CartMapper;
import com.store.book.security.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserEntityRepository userEntityRepository;
    private final CartMapper cartMapper;
    private final ItemService itemService;
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    private final BillGeneratorService billGeneratorService;

    public CartDtoResponse get() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("CartNotFound", null, locale)));
        return cartMapper.entityToDto(cart);
    }

    @Transactional
    public CartDtoResponse buy() {
        final Locale locale = LocaleContextHolder.getLocale();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("CartNotFound", null, locale)));
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : cart.getItem()) {
            itemService.buyItem(item);
            total = total.add((BigDecimal.valueOf(item.getQuantity()).multiply(item.getBook().getNewPrice())));
        }
        if (!(buyBooks(user, total))) {
            throw new NotEnoughMoneyException(
                    messageSource.getMessage("NotEnoughMoneyErrorMessage", null, locale));
        }
        cart.setStatus(Status.BOUGHT);
        billGeneratorService.save(cart);
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setStatus(Status.ACTIVE);
        cartRepository.save(newCart);
        return cartMapper.entityToDto(cartRepository.save(cart));
    }

    private boolean buyBooks(UserEntity user, BigDecimal amount) {
        BigDecimal totalMoney = user.getMoney();
        if (totalMoney.compareTo(amount) <= 0) {
            return false;
        }
        BigDecimal newMoney = totalMoney.subtract(amount);
        user.setMoney(newMoney);
        userEntityRepository.save(user);
        return true;
    }

}
