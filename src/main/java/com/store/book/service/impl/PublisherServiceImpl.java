package com.store.book.service.impl;

import com.store.book.dao.PublisherRepository;
import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.exception.exceptions.EntityContainException;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.PublisherMapper;
import com.store.book.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    public Publisher create(PublisherDtoRequest request) {
        if (publisherRepository.existsPublisherByName(request.getName())) {
            throw new EntityContainException("Publisher already exists");
        }
        return publisherRepository.save(publisherMapper.dtoToEntity(request));
    }

    @Override
    public Publisher getById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found!"));
    }

    @Override
    public List<Publisher> getAll() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher updatePublisher(Long id, PublisherDtoRequest request) {
        Publisher publisher = getById(id);
        publisher.setName(request.getName());
        return publisherRepository.save(publisher);
    }

    @Override
    public Page<Publisher> getAllWithPage(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        Publisher publisher = getById(id);
        publisherRepository.delete(publisher);
    }
}
