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
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface BookMapper {

    Book dtoToEntity(BookDtoRequest bookDtoRequest);
    List<BookDtoResponse> entityToDtoList(List<Book> bookList);

    @Mappings({
            @Mapping(target = "hasDiscount", source = "book", qualifiedByName = "hasDiscount"),
            @Mapping(target = "oldPrice", source = "price"),
            @Mapping(target = "publisherName", source = "publisher.name"),
            @Mapping(target = "authorName", source = "author.name"),
            @Mapping(target = "newPrice", source = "book", qualifiedByName = "newPrice"),
            @Mapping(target = "comments", source = "book.comments"),
            @Mapping(target = "rating", source = "rating", defaultValue = "0")
    })
    BookDtoResponse entityToDto(Book book);

    @Named("hasDiscount")
    default boolean hasDiscount(Book book) {
        if (book.getDiscounts() == null) {
            return false;
        }
        return book.getDiscounts().stream()
                .anyMatch(Discount::isActive);
    }

    @Named("newPrice")
    default BigDecimal newPrice(Book book) {
        List<BigDecimal> percentages = new ArrayList<>();
        BigDecimal newPrice = book.getPrice();
        if (book.getDiscounts() != null && hasDiscount(book)) {
            for (Discount discount : book.getDiscounts()) {
                if (discount.isActive()) {
                    percentages.add(discount.getPercentage());
                }
            }
            for (BigDecimal percentage : percentages) {
                newPrice = newPrice
                        .multiply(BigDecimal.valueOf(100).subtract(percentage).abs())
                        .divide(BigDecimal.valueOf(100));
            }
        }
        return newPrice;
    }
}
