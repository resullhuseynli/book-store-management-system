package com.store.book.service.impl;

import com.store.book.dao.BookRepository;
import com.store.book.dao.CommentRepository;
import com.store.book.dao.dto.CommentDtoRequest;
import com.store.book.dao.dto.CommentDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Comment;
import com.store.book.enums.Status;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.mapper.CommentMapper;
import com.store.book.service.BookService;
import com.store.book.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final MessageSource messageSource;

    @Override
    public CommentDtoResponse getById(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("CommentNotFound", null, locale)));
        return commentMapper.entityToDto(comment);
    }

    @Override
    public Comment getByIdWithDetails(Long id) {
        final Locale locale = LocaleContextHolder.getLocale();
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("CommentNotFound", null, locale)));
    }

    @Override
    public List<CommentDtoResponse> getAll() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .filter(c -> c.getStatus().equals(Status.ACTIVE))
                .map(commentMapper::entityToDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = getByIdWithDetails(id);
        comment.setStatus(Status.DELETED);
        commentRepository.save(comment);
        Book book = bookService.getBookWithDetailsById(comment.getBook().getId());
        BigDecimal size = BigDecimal.valueOf(book.getComments().size());
        BigDecimal newRating = book.getRating()
                .multiply(size.add(BigDecimal.ONE))
                .subtract(BigDecimal.valueOf(comment.getRating()))
                .divide(size, RoundingMode.HALF_UP);
        book.setRating(newRating);
        bookRepository.save(book);
    }

    @Override
    public CommentDtoResponse create(CommentDtoRequest entity) {
        Comment comment = commentMapper.dtoToEntity(entity);
        Comment saved = commentRepository.save(comment);
        return commentMapper.entityToDto(saved);
    }

    @Override
    public List<CommentDtoResponse> getCommentsByBookId(Long bookId) {
        List<Comment> bookComments = commentRepository.getCommentsByBookId(bookId);
        return commentMapper.entityToDto(bookComments);
    }
}
