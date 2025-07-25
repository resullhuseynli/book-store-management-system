package com.store.book.dao;

import com.store.book.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings(value = "NullableProblems")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.book.id = :bookId AND c.status != 'DELETED'")
    List<Comment> getCommentsByBookId(Long bookId);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.status != 'DELETED'")
    Optional<Comment> findById(Long id);

}
