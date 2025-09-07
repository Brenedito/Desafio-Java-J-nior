package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.BookENUM;

public record BookDTO(Long bookId, String title, String author, String isbn, String publishedDate, Integer TotalQuantity, Integer AvailableQuantity, BookENUM status) {
}
