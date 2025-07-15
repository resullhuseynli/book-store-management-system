package com.store.book.service;

import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService extends BaseService<AuthorDtoRequest, Author> {

    Page<Author> getAllAuthors(Pageable pageable);

}
