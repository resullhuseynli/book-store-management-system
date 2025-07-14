package com.store.book.service;

import java.util.Set;

public interface ViewTrackerService {

    void bookTrackView(Long bookId);

    Set<String> getTop10BookIdsForToday();
}
