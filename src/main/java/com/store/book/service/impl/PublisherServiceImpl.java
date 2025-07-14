package com.store.book.service.impl;

import com.store.book.dao.PublisherDAO;
import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.PublisherMapper;
import com.store.book.service.PublisherService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherDAO publisherDAO;
    private final PublisherMapper publisherMapper;

    @Override
    public Publisher create(PublisherDtoRequest request) {
        getAll().forEach(publisher ->
        {
            if (publisher.getName().equals(request.getName()))
                throw new EntityExistsException("Publisher with name " + request.getName() + " already exists");
        });
        return publisherDAO.save(publisherMapper.dtoToEntity(request));
    }

    @Override
    public Publisher getById(Long id) {
        return publisherDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found!"));
    }

    @Override
    public List<Publisher> getAll() {
        return publisherDAO.findAll();
    }

    @Override
    public Publisher updatePublisher(Long id, PublisherDtoRequest request) {
        Publisher publisher = getById(id);
        publisher.setName(request.getName());
        return publisherDAO.save(publisher);
    }

    @Override
    public void deleteById(Long id) {
        Publisher publisher = getById(id);
        publisherDAO.delete(publisher);
    }
}
