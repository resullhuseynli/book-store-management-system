package com.store.book.service;

import com.store.book.dao.entity.Cart;
import com.store.book.dao.model.BillModel;

public interface BillGeneratorService {

    byte[] generateBillPdf(BillModel bill);

    void save(Cart cart);

    byte[] getBillPdf(Long id);
}
