package com.store.book.mapper;

import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface CartMapper {

    @Mapping(target = "items", source = "item")
    @Mapping(target = "totalPrice", source = "cart", qualifiedByName = "calculateTotalPrice")
    CartDtoResponse entityToDto(Cart cart);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(Item item : cart.getItem()) {
            totalPrice = totalPrice
                    .add(
                            BigDecimal.valueOf(item.getQuantity())
                                    .multiply(item.getBook().getNewPrice())
                    );
        }
        return totalPrice;
    }
}
