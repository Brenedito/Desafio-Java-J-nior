package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.ValidationGroups.OnCreate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false, unique = true)
    @Email(groups= OnCreate.class, message = "O email informado é inválido.")
    private String email;


    @Column(nullable = false, unique = true)
    @CPF(groups= OnCreate.class, message = "O CPF informado é inválido.")
    private String cpf;


    @Column(nullable = false)
    private LocalDateTime registration_date;


    @Column(nullable = true)
    private UserENUM status = UserENUM.ATIVO;

    public UserModel(String name, String email, String cpf, LocalDateTime registration_date, UserENUM status) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.registration_date = registration_date;
        this.status = status;
    }
}
