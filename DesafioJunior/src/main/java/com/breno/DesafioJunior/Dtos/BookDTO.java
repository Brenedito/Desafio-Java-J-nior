package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.BookENUM;

import java.time.LocalDateTime;

public record BookDTO(Long bookId, String title, String author, String isbn, LocalDateTime published_date, Integer total_quantity, Integer available_quantity, BookENUM status) {
}
