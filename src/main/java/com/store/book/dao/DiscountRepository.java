package com.store.book.dao;

import com.store.book.dao.entity.Discount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends CrudRepository<Discount,Long> {

    @Query("SELECT d FROM Discount d LEFT JOIN FETCH d.books WHERE d.endDate < :now AND d.isActive = true AND d.status = 'ACTIVE'")
    List<Discount> findExpiredDiscounts(@Param("now") LocalDateTime now);

    @Query("SELECT d FROM Discount d LEFT JOIN FETCH d.books WHERE d.startDate < :now AND d.endDate > :now AND d.isActive = false AND d.status = 'ACTIVE'")
    List<Discount> findActiveDiscounts(@Param("now") LocalDateTime now);

    @Query("SELECT d FROM Discount d WHERE d.isActive = true AND d.status != 'DELETED'")
    List<Discount> findAllByActiveTrue();
}
