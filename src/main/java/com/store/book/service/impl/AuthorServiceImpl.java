package com.store.book.service.impl;

import com.store.book.dao.AuthorRepository;
import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.AuthorMapper;
import com.store.book.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id:" + id + " not found"));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author create(AuthorDtoRequest request) {
        if (authorRepository.existsAuthorsByName(request.getName())) {
            throw new EntityContainException("Author already exists");
        }
        return authorRepository.save(authorMapper.dtoToEntity(request));
    }

    public Author updateAuthor(Long id, AuthorDtoRequest request) {
        Author author = getById(id);
        author.setName(request.getName());
        author.setAboutUrl(request.getAboutUrl());
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(Long id) {
        Author author = getById(id);
        authorRepository.delete(author);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorRepository.getAuthorsByName(name);
    }
}
