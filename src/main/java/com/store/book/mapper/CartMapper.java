package com.store.book.mapper;

import com.store.book.dao.dto.CartDtoResponse;
import com.store.book.dao.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface CartMapper {

    @Mapping(target = "items", source = "item")
    CartDtoResponse entityToDto(Cart cart);
}
