package com.breno.DesafioJunior.Dtos;

import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Utils.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

public record UserDTO(
        Long userId,
        String name,
        @Email(groups= ValidationGroups.OnCreate.class, message = "O email informado é inválido.")
        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "O email não pode ser vazio.")
        String email,
        @CPF(groups = ValidationGroups.OnCreate.class, message = "O CPF informado é inválido.")
        @NotBlank(groups = ValidationGroups.OnCreate.class, message = "O CPF não pode ser vazio.")
        String cpf,
        LocalDateTime registrationDate,
        UserENUM status) {

}
