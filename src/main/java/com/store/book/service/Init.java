package com.store.book.service;

import com.store.book.service.impl.DiscountServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {

    private final DiscountServiceImpl discountService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void checkExpiredDiscounts() {
        discountService.checkExpiredDiscounts();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void checkActiveDiscounts() {
        discountService.checkActiveDiscounts();
    }
}
