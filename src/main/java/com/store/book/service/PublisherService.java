package com.store.book.service;

import com.store.book.dao.PublisherDAO;
import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import com.store.book.mapper.PublisherMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherDAO  publisherDAO;
    private final PublisherMapper publisherMapper;

    public Publisher createPublisher(PublisherDtoRequest request) {
        return publisherDAO.save(publisherMapper.dtoToEntity(request));
    }

    public Publisher getPublisherById(Long id) {
        return publisherDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher with id " + id + " not found!"));
    }

    public List<Publisher> getAllPublishers() {
        return publisherDAO.findAll();
    }

    public Publisher updatePublisher(Long id, PublisherDtoRequest request) {
        Publisher publisher = publisherDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher with id " + id + " not found!"));
        publisher.setName(request.getName());
        return publisherDAO.save(publisher);
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher with id " + id + " not found!"));
        publisherDAO.delete(publisher);
    }
}
