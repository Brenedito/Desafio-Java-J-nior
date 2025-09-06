package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.LoanENUM;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class LoanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @Column(nullable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", nullable = false)
    @Column(nullable = false)
    private BookModel book;

    @Column(nullable = false)
    private LocalDate loanDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Column(nullable = false)
    private LocalDate actualReturnDate;
    @Column(nullable = false)
    private LoanENUM status;

}
