package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.UserENUM;

import java.time.LocalDateTime;

public record UserDTO(Long userId, String name, String email, String cpf, LocalDateTime registrationDate, UserENUM status) {
}
