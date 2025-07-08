package com.store.book.mapper;

import com.store.book.dao.dto.BookDtoRequest;
import com.store.book.dao.dto.BookDtoResponse;
import com.store.book.dao.entity.Book;
import org.springframework.stereotype.Component;

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
        return BookDtoResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genre(book.getGenre())
                .price(book.getPrice())
                .pageCount(book.getPageCount())
                .languages(book.getLanguages())
                .build();
    }
}
