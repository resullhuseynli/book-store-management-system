package com.store.book.mapper;

import com.store.book.dao.dto.CommentDtoRequest;
import com.store.book.dao.dto.CommentDtoResponse;
import com.store.book.dao.entity.Comment;
import com.store.book.dao.entity.UserEntity;
import com.store.book.security.CustomUserDetailsService;
import com.store.book.service.BookService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookService.class, CustomUserDetailsService.class})
public abstract class CommentMapper {

    @Autowired
    private BookService bookService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Mappings({
            @Mapping(target = "book", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    public abstract Comment dtoToEntity(CommentDtoRequest request);

    @AfterMapping
    protected void afterDtoToEntity(CommentDtoRequest request, @MappingTarget Comment comment) {
        comment.setBook(bookService.getBookWithDetailsById(request.getBookId()));

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        comment.setUser(customUserDetailsService.loadUserByUsername(userName));
    }

    @Mappings({
            @Mapping(target = "userName", source = "user", qualifiedByName = "getUserName"),
            @Mapping(target = "bookId", source = "book.id"),
            @Mapping(target = "bookTitle", source = "book.title")
    })
    public abstract CommentDtoResponse entityToDto(Comment entity);

    public abstract List<CommentDtoResponse> entityToDto(List<Comment> entity);

    @Named("getUserName")
    protected String getUserName(UserEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}

