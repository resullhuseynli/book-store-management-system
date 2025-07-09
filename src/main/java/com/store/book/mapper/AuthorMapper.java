package com.store.book.mapper;

import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author dtoToEntity(AuthorDtoRequest request) {
        return Author.builder()
                .aboutUrl(request.getAboutUrl())
                .name(request.getName())
                .build();
    }
}
