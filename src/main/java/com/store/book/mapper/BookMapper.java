package com.store.book.mapper;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookMapper {

    public Book dtoToEntity(BookDtoRequest bookDtoRequest) {
        return Book.builder()
                .title(bookDtoRequest.getTitle())
                .genre(bookDtoRequest.getGenre())
                .price(bookDtoRequest.getPrice())
                .amount(bookDtoRequest.getAmount())
                .pageCount(bookDtoRequest.getPageCount())
                .languages(bookDtoRequest.getLanguages())
                .build();
    }

    public BookDtoResponse entityToDto(Book book) {
        boolean hasDiscount = book.getDiscounts().stream()
                .anyMatch(Discount::isActive);
        BigDecimal percentage = null;
        for (Discount discount : book.getDiscounts()) {
            if (discount.isActive()) {
                percentage = discount.getPercentage();
            }
        }

        BigDecimal newPrice = hasDiscount
                ? book.getPrice()
                .multiply(BigDecimal.valueOf(100).subtract(percentage).abs())
                .divide(BigDecimal.valueOf(100))
                : book.getPrice();

        return BookDtoResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genre(book.getGenre())
                .oldPrice(book.getPrice())
                .hasDiscount(hasDiscount)
                .newPrice(newPrice)
                .pageCount(book.getPageCount())
                .languages(book.getLanguages())
                .publisherName(book.getPublisher().getName())
                .authorName(book.getAuthor().getName())
                .build();
    }
}
