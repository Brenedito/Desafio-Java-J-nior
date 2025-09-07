package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.LoanENUM;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Models.UserModel;

import java.time.LocalDate;

public record LoanDTO(Long loanId, BookModel book, UserModel user, LocalDate loanDate, LocalDate returnDate, LoanENUM status) {
}
