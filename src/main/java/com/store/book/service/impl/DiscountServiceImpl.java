package com.store.book.service.impl;

import com.store.book.dao.BookRepository;
import com.store.book.dao.DiscountRepository;
import com.store.book.dao.dto.DiscountDtoRequest;
import com.store.book.dao.dto.DiscountDtoRequestAll;
import com.store.book.dao.dto.DiscountDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Discount;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.DiscountMapper;
import com.store.book.service.DiscountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final BookRepository bookRepository;
    private final DiscountMapper discountMapper;
    private final BookServiceImpl bookService;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public DiscountDtoResponse create(DiscountDtoRequest request) {
        Discount discount = discountMapper.dtoToEntity(request);
        List<Book> books = new ArrayList<>();
        request.getBookIds().forEach(bookId -> {
            Book book = bookService.getBookWithDetailsById(bookId);
            books.add(book);
            if (book.getDiscounts() == null) book.setDiscounts(new ArrayList<>());
            book.getDiscounts().add(discount);
            calculateNewPrice(book);
        });
        discount.setBooks(books);
        discountRepository.save(discount);
        bookRepository.saveAll(books);
        return discountMapper.entityToDto(discount);
    }

    @Transactional
    public void addAll(DiscountDtoRequestAll request) {
        List<Long> booksIds = bookRepository.findAll().stream()
                .map(Book::getId)
                .toList();
        DiscountDtoRequest dtoRequest = discountMapper.dtoToDto(request);
        dtoRequest.setBookIds(booksIds);
        create(dtoRequest);
    }

    @Transactional
    public void checkActiveDiscounts() {
        List<Discount> expiredDiscounts = discountRepository.findExpiredDiscounts(LocalDateTime.now());
        for (Discount discount : expiredDiscounts) {
            discount.setActive(false);
            discount.getBooks().forEach(this::calculateNewPrice);
        }
        discountRepository.saveAll(expiredDiscounts);
    }

    private void calculateNewPrice(Book book) {
        BigDecimal originalPrice = book.getPrice();
        BigDecimal newPrice = originalPrice;

        for (Discount dis : book.getDiscounts()) {
            if (dis.isActive()) {
                BigDecimal discountAmount = originalPrice
                        .multiply(dis.getPercentage())
                        .divide(BigDecimal.valueOf(100));
                newPrice = newPrice.subtract(discountAmount);
            }
        }
        book.setNewPrice(newPrice);
    }

    @Override
    public List<DiscountDtoResponse> getAllActiveDiscounts() {
        List<Discount> activeDiscountList = discountRepository.findAllByActiveTrue();
        return activeDiscountList.stream()
                .map(discountMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDtoResponse getById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return discountRepository.findById(id)
                .map(discountMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("DiscountNotFound", null, locale)));
    }

    @Override
    public List<DiscountDtoResponse> getAll() {
        List<Discount> discounts = (List<Discount>) discountRepository.findAll();
        return discounts.stream()
                .map(discountMapper::entityToDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("DiscountNotFound", null, locale)));
        discount.setActive(false);
        discount.getBooks().forEach(this::calculateNewPrice);
        discountRepository.save(discount);
    }
}
