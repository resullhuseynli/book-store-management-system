package com.store.book.service.impl;

import com.store.book.dao.AuthorDAO;
import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.AuthorMapper;
import com.store.book.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements BaseService<AuthorDtoRequest, Author> {

    private final AuthorDAO authorDAO;
    private final AuthorMapper authorMapper;

    @Override
    public Author getById(Long id) {
        return authorDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id:" + id + " not found"));
    }

    @Override
    public List<Author> getAll() {
        return authorDAO.findAll();
    }

    @Override
    public Author create(AuthorDtoRequest request) {
        getAll().forEach(author -> {
            if (author.getName().equals(request.getName())) {
                throw new EntityContainException("Author already exists");
            }
        });
        return authorDAO.save(authorMapper.dtoToEntity(request));
    }

    public Author updateAuthor(Long id, AuthorDtoRequest request) {
        Author author = getById(id);
        author.setName(request.getName());
        author.setAboutUrl(request.getAboutUrl());
        return authorDAO.save(author);
    }

    @Override
    public void deleteById(Long id) {
        Author author = getById(id);
        authorDAO.delete(author);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorDAO.getAuthorsByName(name);
    }
}
