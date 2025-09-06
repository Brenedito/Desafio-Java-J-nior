package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.BookENUM;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String isbn;
    @Column(nullable = false)
    private LocalDateTime publishedDate;
    @Column(nullable = false)
    private Integer TotalQuantity;
    @Column(nullable = false)
    private Integer AvailableQuantity;
    @Column(nullable = false)
    private BookENUM status;
}
