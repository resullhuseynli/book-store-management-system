package com.store.book.service;

import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService extends BaseService<PublisherDtoRequest, Publisher> {

    Publisher updatePublisher(Long id, PublisherDtoRequest request);

    Page<Publisher> getAllWithPage(Pageable pageable);
}
