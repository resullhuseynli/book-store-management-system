package com.store.book.service.impl;

import com.store.book.dao.AuthorRepository;
import com.store.book.dao.dto.AuthorDtoRequest;
import com.store.book.dao.entity.Author;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.AuthorMapper;
import com.store.book.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final MessageSource messageSource;

    @Override
    public Author getById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("AuthorNotFound", null, locale)));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author create(AuthorDtoRequest request) {
        final Locale locale = LocaleContextHolder.getLocale();
        if (authorRepository.existsAuthorsByName(request.getName())) {
            throw new EntityContainException(
                    messageSource.getMessage("AuthorContainsMessage",  null, locale));
        }
        return authorRepository.save(authorMapper.dtoToEntity(request));
    }

    @Override
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

    @Override
    public List<Author> getAuthorsByName(String name) {
        return authorRepository.getAuthorsByName(name);
    }

    @Override
    public Page<Author> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }
}
