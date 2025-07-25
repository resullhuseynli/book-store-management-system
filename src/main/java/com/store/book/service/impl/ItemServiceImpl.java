package com.store.book.service.impl;

import com.store.book.dao.BookRepository;
import com.store.book.dao.CartRepository;
import com.store.book.dao.ItemRepository;
import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.NotEnoughBookException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.ItemMapper;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final MessageSource messageSource;

    @Override
    public ItemDtoResponse getById(Long id) {
        Item item = getItemWithDetails(id);
        return itemMapper.entityToDto(item);
    }

    public Item getItemWithDetails(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("ItemNotFound", null, locale)));
    }

    @Override
    public List<ItemDtoResponse> getAll() {
        return itemRepository.findAll().stream()
                .filter(i -> i.getStatus() == Status.ACTIVE)
                .map(itemMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoResponse> getAllItemsByCart() {
        final Locale locale = LocaleContextHolder.getLocale();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("CartNotFound", null, locale)));
        return itemRepository.findAll().stream()
                .filter(cartBook -> cartBook.getCart().equals(cart))
                .filter(i -> i.getStatus() == Status.ACTIVE)
                .map(itemMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void buyItem(Item item) {
        final Locale locale = LocaleContextHolder.getLocale();
        Book book = item.getBook();
        book.setAmount(book.getAmount() - item.getQuantity());
        if (book.getAmount() <= 0) {
            throw new NotEnoughBookException(
                    messageSource.getMessage("NotEnoughBookErrorMessage", null, locale));
        }
        bookRepository.save(book);
        itemMapper.entityToDto(item);
    }

    @Override
    public void deleteById(Long id) {
        Item item = getItemWithDetails(id);
        item.setStatus(Status.DELETED);
        itemRepository.save(item);
    }

    @Override
    public ItemDtoResponse create(ItemDtoRequest entity) {
        Item item = itemMapper.dtoToEntity(entity);
        Item saved = itemRepository.save(item);
        return itemMapper.entityToDto(saved);
    }
}
