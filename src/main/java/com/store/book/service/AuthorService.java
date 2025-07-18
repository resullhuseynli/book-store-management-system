package com.store.book.service;

import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService extends BaseService<AuthorDtoRequest, Author> {

    Page<Author> getAllAuthors(Pageable pageable);
    Author updateAuthor(Long id, AuthorDtoRequest request);
    List<Author> getAuthorsByName(String name);

}
