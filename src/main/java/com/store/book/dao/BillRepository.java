package com.store.book.dao;

import com.store.book.dao.entity.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillRepository extends MongoRepository<Bill,Long> {
}
