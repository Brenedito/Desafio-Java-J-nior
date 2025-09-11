package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.LoanDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Enums.LoanENUM;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Exceptions.AvailabilityException;
import com.breno.DesafioJunior.Exceptions.ResourceNotFoundException;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Models.LoanModel;
import com.breno.DesafioJunior.Models.UserModel;
import com.breno.DesafioJunior.Repositories.BookRepository;
import com.breno.DesafioJunior.Repositories.LoanRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansServiceTest {

    @InjectMocks
    private LoansService loansService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    private UserModel userModel;
    private BookModel bookModel;
    private LoanModel loanModel;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        userModel = new UserModel("Breno", "breno@email.com", "123.456.789-00", LocalDateTime.now(), UserENUM.ATIVO);
        userModel.setUser_id(1L);
        bookModel = new BookModel("O Senhor dos Anéis", "J.R.R. Tolkien", "978-0-261-10238-5", LocalDateTime.now(), 10, 5, BookENUM.DISPONIVEL);
        bookModel.setBook_id(1L);
        loanModel = new LoanModel(userModel, bookModel, LocalDate.now(), LocalDate.now().plusDays(14), null, LoanENUM.ATIVO);
        loanModel.setLoan_id(1L);
        loanDTO = new LoanDTO(1L, bookModel.getBook_id(), userModel.getUser_id(), LocalDate.now(), LocalDate.now().plusDays(14), LoanENUM.ATIVO);
    }

    @Nested
    class ListLoansTests {
        @Test
        @DisplayName("Deve listar os empréstimos com sucesso")
        void listLoans_ShouldReturnListOfLoans() {
            when(loanRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(List.of(loanModel));
            ResponseEntity<List<LoanDTO>> response = loansService.ListLoans(null, 10);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando não há empréstimos")
        void listLoans_WhenNoLoans_ShouldThrowResourceNotFoundException() {
            when(loanRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());
            assertThrows(ResourceNotFoundException.class, () -> loansService.ListLoans(null, 10));
        }
    }

    @Nested
    class FindLoanByUserIdTests {
        @Test
        @DisplayName("Deve encontrar empréstimos pelo ID do utilizador")
        void findLoanByUserId_WhenLoansExist_ShouldReturnLoans() {
            when(loanRepository.FindAllLoansByUserId(anyLong())).thenReturn(List.of(loanModel));
            ResponseEntity<List<LoanDTO>> response = loansService.FindLoanByUserId(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o utilizador não tem empréstimos")
        void findLoanByUserId_WhenNoLoans_ShouldThrowResourceNotFoundException() {
            when(loanRepository.FindAllLoansByUserId(anyLong())).thenReturn(Collections.emptyList());
            assertThrows(ResourceNotFoundException.class, () -> loansService.FindLoanByUserId(1L));
        }
    }

    @Nested
    class RegisterNewLoanTests {
        @Test
        @DisplayName("Deve registar um novo empréstimo com sucesso")
        void registerNewLoan_ShouldSucceed() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(loanRepository.countByUserId(anyLong())).thenReturn(2);
            when(loanRepository.save(any(LoanModel.class))).thenReturn(loanModel);
            ResponseEntity<LoanDTO> response = loansService.RegisterNewLoan(loanDTO);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException se o utilizador não for encontrado")
        void registerNewLoan_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> loansService.RegisterNewLoan(loanDTO));
        }

        @Test
        @DisplayName("Deve lançar AvailabilityException se o livro não estiver disponível")
        void registerNewLoan_WhenBookIsUnavailable_ShouldThrowAvailabilityException() {
            bookModel.setAvailable_quantity(0);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            assertThrows(AvailabilityException.class, () -> loansService.RegisterNewLoan(loanDTO));
        }

        @Test
        @DisplayName("Deve lançar AvailabilityException se o utilizador atingiu o limite")
        void registerNewLoan_WhenUserReachedLoanLimit_ShouldThrowAvailabilityException() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(loanRepository.countByUserId(anyLong())).thenReturn(3);
            assertThrows(AvailabilityException.class, () -> loansService.RegisterNewLoan(loanDTO));
        }
    }

    @Nested
    class RegisterLoanReturnTests {
        @Test
        @DisplayName("Deve registar a devolução de um empréstimo com sucesso")
        void registerLoanReturn_WhenLoanExists_ShouldSucceed() {
            when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loanModel));
            when(loanRepository.save(any(LoanModel.class))).thenReturn(loanModel);
            ResponseEntity<LoanDTO> response = loansService.RegisterLoanReturn(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar devolver um empréstimo inexistente")
        void registerLoanReturn_WhenLoanDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> loansService.RegisterLoanReturn(99L));
        }
    }
}