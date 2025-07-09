package com.store.book.service;

import com.store.book.dao.AuthorDAO;
import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDAO authorDAO;
    private final AuthorMapper authorMapper;

    public Author getAuthorById(Long id) {
        return authorDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id:" + id + " not found"));
    }

    public List<Author> getAllAuthors() {
        return authorDAO.findAll();
    }

    public Author createAuthor(AuthorDtoRequest request) {
        getAllAuthors().forEach(author -> {
            if(author.getName().equals(request.getName())) {
                throw new EntityContainException("Author already exists");
            }
        });
        return authorDAO.save(authorMapper.dtoToEntity(request));
    }

    public Author updateAuthor(Long id, AuthorDtoRequest request) {
        Author author = getAuthorById(id);
        author.setName(request.getName());
        author.setAboutUrl(request.getAboutUrl());
        return  authorDAO.save(author);
    }

    public void deleteAuthorById(Long id) {
        Author author = getAuthorById(id);
        authorDAO.delete(author);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorDAO.getAuthorsByName(name);
    }
}
