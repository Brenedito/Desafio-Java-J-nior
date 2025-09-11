package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Exceptions.DataIntegrityException;
import com.breno.DesafioJunior.Exceptions.ResourceNotFoundException;
import com.breno.DesafioJunior.Models.UserModel;
import com.breno.DesafioJunior.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UserRepository userRepository;

    private UserModel userModel;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userModel = new UserModel("Breno", "breno@email.com", "123.456.789-00", LocalDateTime.now(), UserENUM.ATIVO);
        userModel.setUser_id(1L);
        userDTO = new UserDTO(1L, "Breno", "breno@email.com", "123.456.789-00", LocalDateTime.now(), UserENUM.ATIVO);
    }

    @Nested
    class ListUsersTests {
        @Test
        @DisplayName("Deve listar os utilizadores com sucesso quando 'after' é nulo")
        void listUsers_WhenAfterIsNull_ShouldReturnListOfUsers() {
            when(userRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(List.of(userModel));
            ResponseEntity<List<UserDTO>> response = usersService.ListUsers(null, 10);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando não há utilizadores")
        void listUsers_WhenNoUsers_ShouldThrowResourceNotFoundException() {
            when(userRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());
            assertThrows(ResourceNotFoundException.class, () -> usersService.ListUsers(null, 10));
        }
    }

    @Nested
    class FindUserByIdTests {
        @Test
        @DisplayName("Deve encontrar um utilizador pelo ID com sucesso")
        void findUserById_WhenUserExists_ShouldReturnUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            ResponseEntity<UserDTO> response = usersService.FindUserById(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o utilizador não existe")
        void findUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> usersService.FindUserById(99L));
        }
    }

    @Nested
    class RegisterNewUserTests {
        @Test
        @DisplayName("Deve registar um novo utilizador com sucesso")
        void registerNewUser_ShouldReturnCreatedUser() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByCpf(anyString())).thenReturn(false);
            when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
            ResponseEntity<UserDTO> response = usersService.RegisterNewUser(userDTO);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar DataIntegrityException ao criar com email existente")
        void registerNewUser_WhenEmailExists_ShouldThrowDataIntegrityException() {
            when(userRepository.existsByEmail(anyString())).thenReturn(true);
            assertThrows(DataIntegrityException.class, () -> usersService.RegisterNewUser(userDTO));
        }

        @Test
        @DisplayName("Deve lançar DataIntegrityException ao criar com CPF existente")
        void registerNewUser_WhenCpfExists_ShouldThrowDataIntegrityException() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByCpf(anyString())).thenReturn(true);
            assertThrows(DataIntegrityException.class, () -> usersService.RegisterNewUser(userDTO));
        }
    }

    @Nested
    class UpdateUserInfoTests {
        @Test
        @DisplayName("Deve atualizar as informações de um utilizador com sucesso")
        void updateUserInfo_WhenUserExists_ShouldReturnUpdatedUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
            UserDTO updatedInfo = new UserDTO(null, "Breno Atualizado", null, null, null, UserENUM.INATIVO);
            ResponseEntity<UserDTO> response = usersService.UpdateUserInfo(1L, updatedInfo);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar um utilizador inexistente")
        void updateUserInfo_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> usersService.UpdateUserInfo(99L, userDTO));
        }
    }
}