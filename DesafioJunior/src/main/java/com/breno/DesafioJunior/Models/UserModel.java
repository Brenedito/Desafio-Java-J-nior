package com.breno.DesafioJunior.Models;

import com.breno.DesafioJunior.Enums.UserENUM;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private LocalDateTime registrationDate;
    @Column(nullable = false)
    private UserENUM status;
}
