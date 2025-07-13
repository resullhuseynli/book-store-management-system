package com.store.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewTrackerService {

    private final StringRedisTemplate redisTemplate;

    public void bookTrackView(Long bookId) {
        String key = "book:view:" + bookId;
        redisTemplate.opsForValue().increment(key);
    }

    public Long getVBookViewCount(Long bookId) {
        String key = "book:view:" + bookId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0L;
    }
}
