package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Exceptions.DataIntegrityException;
import com.breno.DesafioJunior.Exceptions.ResourceNotFoundException;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Repositories.BookRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @InjectMocks
    private BooksService booksService;

    @Mock
    private BookRepository bookRepository;

    private BookModel bookModel;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookModel = new BookModel("O Senhor dos Anéis", "J.R.R. Tolkien", "978-0-261-10238-5", LocalDateTime.now(), 10, 5, BookENUM.DISPONIVEL);
        bookModel.setBook_id(1L);
        bookDTO = new BookDTO(1L, "O Senhor dos Anéis", "J.R.R. Tolkien", "978-0-261-10238-5", LocalDateTime.now(), 10, 5, BookENUM.DISPONIVEL);
    }

    @Nested
    class ListBooksTests {
        @Test
        @DisplayName("Deve listar os livros com sucesso quando 'after' é nulo")
        void listBooks_WhenAfterIsNull_ShouldReturnListOfBooks() {
            when(bookRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(List.of(bookModel));
            ResponseEntity<List<BookDTO>> response = booksService.ListBooks(null, 10);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando não há livros")
        void listBooks_WhenNoBooks_ShouldThrowResourceNotFoundException() {
            when(bookRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());
            // Verifica se a exceção correta é lançada
            assertThrows(ResourceNotFoundException.class, () -> booksService.ListBooks(null, 10));
        }
    }

    @Nested
    class FindBookByIdTests {
        @Test
        @DisplayName("Deve encontrar um livro pelo ID com sucesso")
        void findBookById_WhenBookExists_ShouldReturnBook() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            ResponseEntity<List<BookDTO>> response = booksService.FindBookById(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o livro não existe")
        void findBookById_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> booksService.FindBookById(99L));
        }
    }

    @Nested
    class RegisterNewBookTests {
        @Test
        @DisplayName("Deve registar um novo livro com sucesso")
        void registerNewBook_ShouldReturnCreatedBook() {
            when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
            when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);
            ResponseEntity<BookDTO> response = booksService.RegisterNewBook(bookDTO);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve lançar DataIntegrityException ao tentar criar livro com ISBN já existente")
        void registerNewBook_WhenIsbnExists_ShouldThrowDataIntegrityException() {
            when(bookRepository.existsByIsbn(anyString())).thenReturn(true);
            assertThrows(DataIntegrityException.class, () -> booksService.RegisterNewBook(bookDTO));
        }
    }

    @Nested
    class UpdateBookInfoTests {
        @Test
        @DisplayName("Deve atualizar as informações de um livro com sucesso")
        void updateBookInfo_WhenBookExists_ShouldReturnUpdatedBook() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
            when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);
            BookDTO updatedInfo = new BookDTO(null, "Novo Título", "Novo Autor", "111-1-111-11111-1", null, null, null, null);
            ResponseEntity<BookDTO> response = booksService.UpdateBookInfo(1L, updatedInfo);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar um livro inexistente")
        void updateBookInfo_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> booksService.UpdateBookInfo(99L, bookDTO));
        }

        @Test
        @DisplayName("Deve lançar DataIntegrityException ao tentar atualizar com ISBN já existente")
        void updateBookInfo_WhenIsbnExists_ShouldThrowDataIntegrityException() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(bookRepository.existsByIsbn(anyString())).thenReturn(true);
            BookDTO updatedInfoWithExistingIsbn = new BookDTO(null, null, null, "123-4-567-89012-3", null, null, null, null);
            assertThrows(DataIntegrityException.class, () -> booksService.UpdateBookInfo(1L, updatedInfoWithExistingIsbn));
        }
    }

    @Nested
    class DeleteBookTests {
        @Test
        @DisplayName("Deve apagar um livro com sucesso")
        void deleteBook_WhenBookExists_ShouldReturnOk() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            doNothing().when(bookRepository).deleteById(anyLong());
            ResponseEntity<Void> response = booksService.DeleteBook(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(bookRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar apagar um livro inexistente")
        void deleteBook_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> booksService.DeleteBook(99L));
            verify(bookRepository, never()).deleteById(anyLong());
        }
    }
}