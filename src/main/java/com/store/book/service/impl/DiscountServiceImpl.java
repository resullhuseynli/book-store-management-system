package com.store.book.service.impl;

import com.store.book.dao.BookDAO;
import com.store.book.dao.DiscountDAO;
import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.DiscountMapper;
import com.store.book.service.BaseService;
import com.store.book.service.DiscountService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountDAO discountDAO;
    private final BookDAO bookDAO;
    private final DiscountMapper discountMapper;
    private final BookServiceImpl bookService;

    @Transactional
    @Override
    public DiscountDtoResponse create(DiscountDtoRequest request) {
        Discount discount = discountMapper.dtoToEntity(request);
        discount.setActive(LocalDateTime.now().isAfter(discount.getStartDate()) && LocalDateTime.now().isBefore(discount.getEndDate()));
        Set<Book> books = new HashSet<>();
        request.getBookIds().forEach(bookId -> {
            Book book = bookService.getBookWithDetailsById(bookId);
            books.add(book);
            if (book.getDiscounts() == null) book.setDiscounts(new HashSet<>());
            book.getDiscounts().add(discount);
        });
        discount.setBooks(books);
        discountDAO.save(discount);
        bookDAO.saveAll(books);
        return discountMapper.entityToDto(discount);
    }

    @PostConstruct
    public void checkActiveDiscounts() {
        List<Discount> activeDiscounts = discountDAO.findExpiredDiscounts(LocalDateTime.now());
        activeDiscounts.forEach(discount -> discount.setActive(false));
        discountDAO.saveAll(activeDiscounts);
    }

    public List<DiscountDtoResponse> getAllActiveDiscounts() {
        List<Discount> activeDiscountList = discountDAO.findAllByActiveTrue();
        return activeDiscountList.stream().map(discountMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public DiscountDtoResponse getById(Long id) {
        return null;
    }

    @Override
    public List<DiscountDtoResponse> getAll() {
        List<Discount> discounts = (List<Discount>) discountDAO.findAll();
        return discounts.stream()
                .map(discountMapper::entityToDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Discount discount = discountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("discount with id: " + id + " not found"));
        discount.setActive(false);
        discountDAO.save(discount);
    }
}
