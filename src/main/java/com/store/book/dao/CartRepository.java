package com.store.book.dao;

import com.store.book.dao.entity.Cart;
import com.store.book.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c WHERE c.user = :user AND c.status = 'ACTIVE'")
    Optional<Cart> findByUser(UserEntity user);
}
