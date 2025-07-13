package com.store.book.mapper;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book dtoToEntity(BookDtoRequest bookDtoRequest);

    @Mappings({
            @Mapping(target = "hasDiscount", source = "book", qualifiedByName = "hasDiscount"),
            @Mapping(target = "oldPrice", source = "price"),
            @Mapping(target = "publisherName", source = "publisher.name"),
            @Mapping(target = "authorName", source = "author.name"),
            @Mapping(target = "newPrice", source = "book", qualifiedByName = "newPrice")
    })
    BookDtoResponse entityToDto(Book book);

    @Named("hasDiscount")
    default boolean hasDiscount(Book book) {
        return book.getDiscounts().stream()
                .anyMatch(Discount::isActive);
    }

    @Named("newPrice")
    default BigDecimal newPrice(Book book) {
        BigDecimal percentage = null;
        for (Discount discount : book.getDiscounts()) {
            if (discount.isActive()) {
                percentage = discount.getPercentage();
            }
        }
        if (hasDiscount(book)) {
            return book.getPrice()
                    .multiply(BigDecimal.valueOf(100).subtract(percentage).abs())
                    .divide(BigDecimal.valueOf(100));
        } else {
            return book.getPrice();
        }
    }
}
