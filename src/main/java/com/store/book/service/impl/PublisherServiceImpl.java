package com.store.book.service.impl;

import com.store.book.dao.PublisherRepository;
import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.PublisherMapper;
import com.store.book.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    private final MessageSource messageSource;

    @Override
    public Publisher create(PublisherDtoRequest request) {
        final Locale locale = LocaleContextHolder.getLocale();
        if (publisherRepository.existsPublisherByName(request.getName())) {
            throw new EntityContainException(
                    messageSource.getMessage("PublisherContainsMessage", null, locale));
        }
        return publisherRepository.save(publisherMapper.dtoToEntity(request));
    }

    @Override
    public Publisher getById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("PublisherNotFound", null, locale)));
    }

    @Override
    public List<Publisher> getAll() {
        return publisherRepository.findAll().stream()
                .filter(p -> p.getStatus().equals(Status.ACTIVE))
                .toList();
    }

    @Override
    public Publisher updatePublisher(Long id, PublisherDtoRequest request) {
        Publisher publisher = getById(id);
        publisher.setName(request.getName());
        return publisherRepository.save(publisher);
    }

    @Override
    public Page<Publisher> getAllWithPage(Pageable pageable) {
        Page<Publisher> page = publisherRepository.findAll(pageable);

        List<Publisher> filteredList = page.getContent().stream()
                .filter(p -> p.getStatus().equals(Status.ACTIVE))
                .toList();

        return new PageImpl<>(filteredList, pageable, filteredList.size());
    }

    @Override
    public void deleteById(Long id) {
        Publisher publisher = getById(id);
        publisher.setStatus(Status.DELETED);
        publisherRepository.save(publisher);
    }
}
