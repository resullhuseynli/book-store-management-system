package com.store.book.mapper;

import com.store.book.dao.CartRepository;
import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.entity.UserEntity;
import com.store.book.enums.Status;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CartRepository cartRepository;

    @Mappings({
            @Mapping(target = "book", source = "request", qualifiedByName = "getBook"),
            @Mapping(target = "cart", source = "request", qualifiedByName = "getCart")
    })
    public abstract Item dtoToEntity(ItemDtoRequest request);

    @Mappings({
            @Mapping(target = "bookName", source = "book.title"),
            @Mapping(target = "price", source = "book.newPrice"),
            @Mapping(target = "totalPrice", source = "item", qualifiedByName = "getItemTotalPrice")
    })
    public abstract ItemDtoResponse entityToDto(Item item);

    @Named("getItemTotalPrice")
    BigDecimal getItemTotalPrice(Item item) {
        Book book = item.getBook();
        return book.getNewPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    @Named("getBook")
    Book getBook(ItemDtoRequest request) {
        return bookService.getBookWithDetailsById(request.getBookId());
    }

    @Named("getCart")
    Cart getCart(ItemDtoRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setStatus(Status.ACTIVE);
            return cartRepository.save(newCart);
        });
    }
}
