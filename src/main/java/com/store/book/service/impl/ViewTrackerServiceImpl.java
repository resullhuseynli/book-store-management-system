package com.store.book.service.impl;

import com.store.book.service.ViewTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewTrackerServiceImpl implements ViewTrackerService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void bookTrackView(Long bookId) {
        LocalDate date = LocalDate.now();
        String key = "book:" + date + ":viewCount";
        redisTemplate.opsForZSet().incrementScore(key, bookId.toString(), 1);
    }

    @Override
    public Set<String> getTop10BookIdsForToday() {
        LocalDate date = LocalDate.now();
        String key = "book:" + date + ":viewCount";
        return redisTemplate.opsForZSet().reverseRange(key, 0, 9);
    }

    public Set<String> getTop10BooksLast7Days() {
        Map<String, Double> bookMap = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String key = "book:" + date + ":viewCount";
            Set<String> bookIds = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
            if(bookIds!= null){
                for(String bookId : bookIds) {
                    Double bookViewCount = redisTemplate.opsForZSet().score(key, bookId);
                    if(bookViewCount != null){
                        bookMap.merge(bookId, bookViewCount, Double::sum);
                    }
                }
            }
        }
        return bookMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
