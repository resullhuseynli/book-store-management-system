package com.store.book.service;

import java.util.List;

public interface BaseService<T, R> {
    R getById(Long id);
    List<R> getAll();
    void deleteById(Long id);
    R create(T entity);
}
