package com.store.book.mapper;

import com.store.book.dao.entity.Item;
import com.store.book.dao.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "name", source = "item.book.title"),
            @Mapping(target = "amount", source = "quantity"),
            @Mapping(target = "price", source = "item", qualifiedByName = "calculatePrice")
    })
    ProductModel itemToProduct(Item item);

    @Named("calculatePrice")
    default BigDecimal calculatePrice(Item item) {
        return item.getBook().getNewPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
