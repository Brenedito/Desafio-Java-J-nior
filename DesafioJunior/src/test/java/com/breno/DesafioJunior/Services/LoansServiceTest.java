package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.LoanDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Enums.LoanENUM;
import com.breno.DesafioJunior.Enums.UserENUM;
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
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
            assertEquals(1L, response.getBody().get(0).userid());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando não há empréstimos")
        void listLoans_WhenNoLoans_ShouldReturnNotFound() {
            when(loanRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());

            ResponseEntity<List<LoanDTO>> response = loansService.ListLoans(null, 10);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando o utilizador não tem empréstimos")
        void findLoanByUserId_WhenNoLoans_ShouldReturnNotFound() {
            when(loanRepository.FindAllLoansByUserId(anyLong())).thenReturn(Collections.emptyList());

            ResponseEntity<List<LoanDTO>> response = loansService.FindLoanByUserId(1L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
            assertEquals(1, response.getBody().loanId());
            verify(bookRepository, times(1)).save(any(BookModel.class));
        }

        @Test
        @DisplayName("Deve falhar ao registar empréstimo se o utilizador não for encontrado")
        void registerNewLoan_WhenUserNotFound_ShouldReturnNotFound() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<LoanDTO> response = loansService.RegisterNewLoan(loanDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve falhar ao registar empréstimo se o livro não for encontrado")
        void registerNewLoan_WhenBookNotFound_ShouldReturnNotFound() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<LoanDTO> response = loansService.RegisterNewLoan(loanDTO);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve falhar ao registar empréstimo se o livro não estiver disponível")
        void registerNewLoan_WhenBookIsUnavailable_ShouldReturnBadRequest() {
            bookModel.setAvailable_quantity(0);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));

            ResponseEntity<LoanDTO> response = loansService.RegisterNewLoan(loanDTO);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve falhar ao registar empréstimo se o utilizador atingiu o limite")
        void registerNewLoan_WhenUserReachedLoanLimit_ShouldReturnBadRequest() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userModel));
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(loanRepository.countByUserId(anyLong())).thenReturn(3);

            ResponseEntity<LoanDTO> response = loansService.RegisterNewLoan(loanDTO);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
            assertNotNull(response.getBody());
            assertEquals(LoanENUM.DEVOLVIDO, response.getBody().status());
            assertNotNull(loanModel.getActual_return_date());
            verify(bookRepository, times(1)).save(any(BookModel.class));
        }

        @Test
        @DisplayName("Deve retornar Not Found ao tentar devolver um empréstimo inexistente")
        void registerLoanReturn_WhenLoanDoesNotExist_ShouldReturnNotFound() {
            when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<LoanDTO> response = loansService.RegisterLoanReturn(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}