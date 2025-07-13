package com.store.book.mapper;

import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    Discount dtoToEntity(DiscountDtoRequest request);

    @Mappings({
            @Mapping(target = "bookNames", source = "entity", qualifiedByName = "extractBookNames"),
            @Mapping(target = "isActive", source = "active")
    })
    DiscountDtoResponse entityToDto(Discount entity);

    @Named("extractBookNames")
    default List<String> extractBookNames(Discount discount) {
        return discount.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
