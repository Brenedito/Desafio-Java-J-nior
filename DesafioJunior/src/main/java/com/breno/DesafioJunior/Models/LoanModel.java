package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.LoanENUM;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "loan")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserModel user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", nullable = false)
    private BookModel book;


    @Column(nullable = false)
    private LocalDate loan_date;


    @Column(nullable = false)
    private LocalDate return_date;


    @Column(nullable = true)
    private LocalDate actual_return_date;


    @Column(nullable = true)
    private LoanENUM status = LoanENUM.ATIVO;

    public LoanModel(UserModel user_id, BookModel book_id, LocalDate loan_date, LocalDate return_date, LocalDate actual_return_date, LoanENUM status) {
        this.user = user_id;
        this.book = book_id;
        this.loan_date = loan_date;
        this.return_date = return_date;
        this.actual_return_date = actual_return_date;
        this.status = status;
    }
}
