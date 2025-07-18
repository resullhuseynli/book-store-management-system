package com.store.book.mapper;

import com.store.book.dao.BookRepository;
import com.store.book.dao.dto.CommentDtoRequest;
import com.store.book.dao.dto.CommentDtoResponse;
import com.store.book.dao.entity.Book;
import com.store.book.dao.entity.Comment;
import com.store.book.dao.entity.UserEntity;
import com.store.book.exception.exceptions.NotFoundException;
import com.store.book.security.CustomUserDetailsService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BookRepository.class, CustomUserDetailsService.class})
public abstract class CommentMapper {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Mappings({
            @Mapping(target = "book", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    public abstract Comment dtoToEntity(CommentDtoRequest request);

    @AfterMapping
    protected void afterDtoToEntity(CommentDtoRequest request, @MappingTarget Comment comment) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        comment.setBook(book);
        if(book.getRating() == null) {
            book.setRating(BigDecimal.valueOf(comment.getRating()));
        } else {
            BigDecimal length = BigDecimal.valueOf(book.getComments().size());
            BigDecimal newRating = book.getRating()
                    .multiply(length)
                    .add(BigDecimal.valueOf(request.getRating()))
                    .divide(length.add(BigDecimal.ONE), RoundingMode.HALF_UP);
            book.setRating(newRating);
        }
        bookRepository.save(book);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        comment.setUser(customUserDetailsService.loadUserByUsername(userName));
    }

    @Mappings({
            @Mapping(target = "userName", source = "user", qualifiedByName = "getUserName")
    })
    public abstract CommentDtoResponse entityToDto(Comment entity);

    public abstract List<CommentDtoResponse> entityToDto(List<Comment> entity);

    @Named("getUserName")
    protected String getUserName(UserEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}

