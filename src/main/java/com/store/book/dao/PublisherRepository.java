package com.store.book.dao;

import com.store.book.dao.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings(value = "NullableProblems")
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsPublisherByName(String name);

    @Query("SELECT p FROM Publisher p WHERE p.id = :id AND p.status != 'DELETED'")
    Optional<Publisher> findById(Long id);
}
