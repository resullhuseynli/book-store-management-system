package com.store.book.dao;

import com.store.book.dao.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings(value = "NullableProblems")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i from Item i WHERE i.id = :id AND i.status != 'DELETED'")
    Optional<Item> findById(Long id);
}
