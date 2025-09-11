package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Enums.UserENUM;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
            assertEquals(1, response.getBody().size());
            assertEquals("Breno", response.getBody().get(0).name());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando 'after' é nulo e não há utilizadores")
        void listUsers_WhenAfterIsNullAndNoUsers_ShouldReturnNotFound() {
            when(userRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());

            ResponseEntity<List<UserDTO>> response = usersService.ListUsers(null, 10);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve listar os utilizadores com sucesso quando 'after' tem um valor")
        void listUsers_WhenAfterHasValue_ShouldReturnListOfUsers() {
            when(userRepository.findByUserIdGreaterThanOrderByIdAsc(anyLong(), any(Pageable.class))).thenReturn(List.of(userModel));

            ResponseEntity<List<UserDTO>> response = usersService.ListUsers(0L, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
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
            assertEquals(1L, response.getBody().userId());
            assertEquals("Breno", response.getBody().name());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando o utilizador não existe")
        void findUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<UserDTO> response = usersService.FindUserById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class RegisterNewUserTests {
        @Test
        @DisplayName("Deve registar um novo utilizador com sucesso")
        void registerNewUser_ShouldReturnCreatedUser() {
            when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

            ResponseEntity<UserDTO> response = usersService.RegisterNewUser(userDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("breno@email.com", response.getBody().email());
        }
    }

    @Nested
    class UpdateUserInfoTests {
        @Test
        @DisplayName("Deve atualizar as informações de um utilizador com sucesso")
        void updateUserInfo_WhenUserExists_ShouldReturnUpdatedUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
            UserDTO updatedInfo = new UserDTO(null, "Breno Atualizado", "breno_novo@email.com", null, null, UserENUM.INATIVO);

            ResponseEntity<UserDTO> response = usersService.UpdateUserInfo(1L, updatedInfo);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Breno Atualizado", response.getBody().name());
            assertEquals("breno_novo@email.com", response.getBody().email());
            assertEquals(UserENUM.INATIVO, response.getBody().status());
        }

        @Test
        @DisplayName("Deve retornar Not Found ao tentar atualizar um utilizador inexistente")
        void updateUserInfo_WhenUserDoesNotExist_ShouldReturnNotFound() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            UserDTO updatedInfo = new UserDTO(null, "Novo Nome", null, null, null, null);

            ResponseEntity<UserDTO> response = usersService.UpdateUserInfo(99L, updatedInfo);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}