package com.store.book.dao;

import com.store.book.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings(value = "NullableProblems")
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String username);

    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.status != 'DELETED'")
    Optional<UserEntity> findById(Long id);
}
