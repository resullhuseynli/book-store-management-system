package com.store.book.dao;

import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("NullableProblems")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> getBooksByGenre(Genre genre);

    List<Book> getBooksByAuthor(Author author);

    @Query("SELECT b FROM Book b WHERE b.id = :id AND b.status != 'DELETED'")
    Optional<Book> findById(Long id);
}
