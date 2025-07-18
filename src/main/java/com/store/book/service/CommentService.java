package com.store.book.service;

import com.store.book.dao.dto.CommentDtoRequest;
import com.store.book.dao.dto.CommentDtoResponse;

import java.util.List;

public interface CommentService extends BaseService<CommentDtoRequest, CommentDtoResponse> {

    List<CommentDtoResponse> getCommentsByBookId(Long bookId);
}
