package com.store.book.dao;

import com.store.book.dao.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> getAuthorsByName(String name);
    boolean existsAuthorsByName(String username);

}
