package com.store.book.dao;

import com.store.book.dao.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherDAO extends JpaRepository<Publisher, Long> {

    boolean existsPublisherByName(String name);
}
