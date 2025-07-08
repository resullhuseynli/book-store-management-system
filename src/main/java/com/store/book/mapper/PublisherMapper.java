package com.store.book.mapper;

import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public Publisher dtoToEntity(PublisherDtoRequest request){
        return Publisher.builder()
                .name(request.getName())
                .build();
    }
}
