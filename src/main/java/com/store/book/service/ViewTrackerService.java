package com.store.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewTrackerService {

    private final StringRedisTemplate redisTemplate;

    public void bookTrackView(Long bookId) {
        LocalDate date = LocalDate.now();
        String key = "book:" + date + ":viewCount";
        redisTemplate.opsForZSet().incrementScore(key, bookId.toString(), 1);
    }

    public Set<String> getTop10BookIds() {
        LocalDate date = LocalDate.now();
        String key = "book:" + date + ":viewCount";
        return redisTemplate.opsForZSet().reverseRange(key, 0, 9);
    }
}
