package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.BookENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "book")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String author;


    @Column(nullable = false)
    private String isbn;


    @Column(nullable = false)
    private LocalDateTime published_date;


    @Column(nullable = false)
    @Min(value = 0, message = "A quantidade total não pode ser negativa.")
    private Integer total_quantity;


    @Column(nullable = false)
    @Min(value = 0, message = "A quantidade disponível não pode ser negativa.")
    private Integer available_quantity;


    @Column(nullable = true)
    private BookENUM status;

    public BookModel(String title, String author, String isbn, LocalDateTime published_date, Integer total_quantity, Integer available_quantity, BookENUM status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.published_date = published_date;
        this.total_quantity = total_quantity;
        this.available_quantity = available_quantity;
        this.status = status;
    }
}
