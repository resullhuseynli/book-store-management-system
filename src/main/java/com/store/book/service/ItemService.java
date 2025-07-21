package com.store.book.service;

import com.store.book.dao.dto.ItemDtoRequest;
import com.store.book.dao.dto.ItemDtoResponse;
import com.store.book.dao.entity.Item;

import java.util.List;

public interface ItemService extends BaseService<ItemDtoRequest, ItemDtoResponse> {

    List<ItemDtoResponse> getAllItemsByCart();

    void buyItem(Item item);

}
