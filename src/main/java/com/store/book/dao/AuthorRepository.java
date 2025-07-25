package com.store.book.dao;

import com.store.book.dao.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings(value = "NullableProblems")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> getAuthorsByName(String name);

    boolean existsAuthorsByName(String username);

    @Query("SELECT a from Author a WHERE a.id = :id AND a.status != 'DELETED'")
    Optional<Author> findById(Long id);
}
