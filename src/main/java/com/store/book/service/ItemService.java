package com.store.book.service;

import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;

import java.util.List;

public interface ItemService extends BaseService<ItemDtoRequest, ItemDtoResponse>{

    List<ItemDtoResponse> getAllItemsByCart();

}
