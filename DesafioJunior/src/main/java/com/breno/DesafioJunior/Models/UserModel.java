package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.UserENUM;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    private String email;


    @Column(nullable = false, unique = true)
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
