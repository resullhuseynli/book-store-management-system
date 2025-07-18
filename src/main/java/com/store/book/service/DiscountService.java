package com.store.book.service;

import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoRequestAll;
import com.store.book.dao.dto.DiscountDtoResponse;

import java.util.List;

public interface DiscountService extends BaseService<DiscountDtoRequest, DiscountDtoResponse>{

    List<DiscountDtoResponse> getAllActiveDiscounts();

    void addAll(DiscountDtoRequestAll request);
}
