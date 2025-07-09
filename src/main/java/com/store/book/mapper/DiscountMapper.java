package com.store.book.mapper;

import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class DiscountMapper {

    public Discount dtoToEntity(DiscountDtoRequest request) {
        return Discount.builder()
                .startDate(LocalDateTime.parse(request.getStartDate()))
                .endDate(LocalDateTime.parse(request.getEndDate()))
                .percentage(request.getPercentage())
                .build();
    }

    public DiscountDtoResponse entityToDto(Discount entity) {
        return DiscountDtoResponse.builder()
                .id(entity.getId())
                .percentage(entity.getPercentage())
                .bookNames(
                        entity.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
