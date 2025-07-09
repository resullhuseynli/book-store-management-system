package com.store.book.dao;

import com.store.book.dao.entity.Discount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountDAO extends CrudRepository<Discount,Long> {
}
