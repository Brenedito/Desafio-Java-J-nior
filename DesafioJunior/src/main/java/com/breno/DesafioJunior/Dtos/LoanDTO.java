package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.LoanENUM;


import java.time.LocalDate;

public record LoanDTO(Long loanId, Long bookid, Long userid, LocalDate loanDate, LocalDate returnDate, LoanENUM status) {
}
