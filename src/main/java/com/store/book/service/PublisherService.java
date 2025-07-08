package com.store.book.service;

import com.store.book.dao.PublisherDAO;
import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.PublisherMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherDAO publisherDAO;
    private final PublisherMapper publisherMapper;

    public Publisher createPublisher(PublisherDtoRequest request) {
        getAllPublishers().forEach(publisher ->
        {
            if (publisher.getName().equals(request.getName()))
                throw new EntityExistsException("Publisher with name " + request.getName() + " already exists");
        });
        return publisherDAO.save(publisherMapper.dtoToEntity(request));
    }

    public Publisher getPublisherById(Long id) {
        return publisherDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found!"));
    }

    public List<Publisher> getAllPublishers() {
        return publisherDAO.findAll();
    }

    public Publisher updatePublisher(Long id, PublisherDtoRequest request) {
        Publisher publisher = getPublisherById(id);
        publisher.setName(request.getName());
        return publisherDAO.save(publisher);
    }

    public void deletePublisher(Long id) {
        Publisher publisher = getPublisherById(id);
        publisherDAO.delete(publisher);
    }
}
