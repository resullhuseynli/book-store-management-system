package com.store.book.dao.entity;

import com.store.book.enums.Genre;
import com.store.book.enums.Language;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_title", length = 100)
    private String title;

    @Column(name = "book_cover_image_url")
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_genre")
    private Genre genre;

    @Column(name = "book_price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "book_amount")
    private Integer amount;

    @Column(name = "book_rating", precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "book_page_count", updatable = false)
    private Integer pageCount;

    @CreationTimestamp
    @Column(name = "book_created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "book_updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    private Author author;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "books_languages",
            joinColumns = @JoinColumn(name = "book_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "book_language")
    private Set<Language> languages;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisher_id")
    private Publisher publisher;
}
