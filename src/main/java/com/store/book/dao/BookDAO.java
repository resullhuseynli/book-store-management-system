package com.store.book.dao;

import com.store.book.dao.entity.Author;
import com.store.book.dao.entity.Book;
import com.store.book.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDAO extends JpaRepository<Book, Long> {

    List<Book> getBooksByGenre(Genre genre);
    List<Book> getBooksByAuthor(Author author);
}
