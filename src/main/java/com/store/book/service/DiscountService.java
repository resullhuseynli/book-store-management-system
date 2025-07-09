package com.store.book.service;

import com.store.book.dao.BookDAO;
import com.store.book.dao.DiscountDAO;
import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import com.store.book.mapper.DiscountMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountDAO discountDAO;
    private final BookDAO bookDAO;
    private final DiscountMapper discountMapper;
    private final BookService bookService;

    @Transactional
    public DiscountDtoResponse createDiscount(DiscountDtoRequest request) {
        Discount discount = discountMapper.dtoToEntity(request);
        Set<Book> books = new HashSet<>();
        request.getBookIds().forEach(bookId -> {
            Book book = bookService.getBookWithDetailsById(bookId);
            books.add(book);
            if(book.getDiscounts()==null) book.setDiscounts(new HashSet<>());
            book.getDiscounts().add(discount);
        });
        discount.setBooks(books);
        discountDAO.save(discount);
        bookDAO.saveAll(books);
        return discountMapper.entityToDto(discount);
    }
}
