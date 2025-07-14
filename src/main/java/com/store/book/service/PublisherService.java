package com.store.book.service;

import com.store.book.dao.dto.PublisherDtoRequest;
import com.store.book.dao.entity.Publisher;

public interface PublisherService extends BaseService<PublisherDtoRequest, Publisher> {

    Publisher updatePublisher(Long id, PublisherDtoRequest request);
}
