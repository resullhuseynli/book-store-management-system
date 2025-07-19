package com.store.book.mapper;

import com.store.book.dao.CartRepository;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import com.store.book.dao.entity.UserEntity;
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
import java.util.Optional;

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
            @Mapping(target = "itemPrice", source = "request", qualifiedByName = "getPrice"),
            @Mapping(target = "itemTotalPrice", source = "request", qualifiedByName = "getTotalPrice"),
            @Mapping(target = "cart", source = "request", qualifiedByName = "getCart")
    })
    public abstract Item dtoToEntity(ItemDtoRequest request);

    @Mappings({
            @Mapping(target = "bookName", source = "book.title"),
            @Mapping(target = "price", source = "itemPrice"),
            @Mapping(target = "totalPrice", source = "itemTotalPrice")
    })
    public abstract ItemDtoResponse entityToDto(Item item);

    @Named("getBook")
    Book getBook(ItemDtoRequest request) {
        return bookService.getBookWithDetailsById(request.getBookId());
    }

    @Named("getPrice")
    BigDecimal getPrice(ItemDtoRequest request) {
        Book book = getBook(request);
        BookDtoResponse response = bookMapper.entityToDto(book);
        return response.getNewPrice();
    }

    @Named("getTotalPrice")
    BigDecimal getTotalPrice(ItemDtoRequest request) {
        return getPrice(request).multiply(BigDecimal.valueOf(request.getQuantity()));
    }

    @Named("getCart")
    Cart getCart(ItemDtoRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = customUserDetailsService.loadUserByUsername(username);
        Optional<Cart> cart = cartRepository.findByUser(user);
        if (cart.isPresent()) {
            if (cart.get().getTotalPrice() == null) {
                cart.get().setTotalPrice(BigDecimal.ZERO);
            }
            cart.get().setTotalPrice(cart.get().getTotalPrice().add(getTotalPrice(request)));
            cartRepository.save(cart.get());
            return cart.get();
        }
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setTotalPrice(getTotalPrice(request));
        cartRepository.save(newCart);
        return newCart;
    }
}
