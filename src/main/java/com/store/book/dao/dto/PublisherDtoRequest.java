package com.store.book.dao.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDtoRequest {

    @Size(min = 1, max = 100)
    private String name;
}
