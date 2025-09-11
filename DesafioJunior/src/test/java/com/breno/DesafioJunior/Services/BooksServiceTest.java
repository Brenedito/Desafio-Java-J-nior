package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
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
            assertFalse(response.getBody().isEmpty());
            assertEquals(1, response.getBody().size());
            assertEquals("O Senhor dos Anéis", response.getBody().get(0).title());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando 'after' é nulo e não há livros")
        void listBooks_WhenAfterIsNullAndNoBooks_ShouldReturnNotFound() {
            when(bookRepository.findByOrderByIdAsc(any(Pageable.class))).thenReturn(Collections.emptyList());

            ResponseEntity<List<BookDTO>> response = booksService.ListBooks(null, 10);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve listar os livros com sucesso quando 'after' tem um valor")
        void listBooks_WhenAfterHasValue_ShouldReturnListOfBooks() {
            when(bookRepository.findByBookIdGreaterThanOrderByIdAsc(anyLong(), any(Pageable.class))).thenReturn(List.of(bookModel));

            ResponseEntity<List<BookDTO>> response = booksService.ListBooks(2L, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
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
            assertEquals(1L, response.getBody().get(0).bookId());
        }

        @Test
        @DisplayName("Deve retornar Not Found quando o livro não existe")
        void findBookById_WhenBookDoesNotExist_ShouldReturnNotFound() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<List<BookDTO>> response = booksService.FindBookById(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class RegisterNewBookTests {
        @Test
        @DisplayName("Deve registar um novo livro com sucesso")
        void registerNewBook_ShouldReturnCreatedBook() {
            when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);

            ResponseEntity<BookDTO> response = booksService.RegisterNewBook(bookDTO);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("J.R.R. Tolkien", response.getBody().author());
        }
    }

    @Nested
    class UpdateBookInfoTests {
        @Test
        @DisplayName("Deve atualizar as informações de um livro com sucesso")
        void updateBookInfo_WhenBookExists_ShouldReturnUpdatedBook() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookModel));
            when(bookRepository.save(any(BookModel.class))).thenReturn(bookModel);
            BookDTO updatedInfo = new BookDTO(null, "Novo Título", "Novo Autor", null, null, null, null, null);

            ResponseEntity<BookDTO> response = booksService.UpdateBookInfo(1L, updatedInfo);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Novo Título", response.getBody().title());
            assertEquals("Novo Autor", response.getBody().author());
        }

        @Test
        @DisplayName("Deve retornar Not Found ao tentar atualizar um livro inexistente")
        void updateBookInfo_WhenBookDoesNotExist_ShouldReturnNotFound() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
            BookDTO updatedInfo = new BookDTO(null, "Novo Título", null, null, null, null, null, null);

            ResponseEntity<BookDTO> response = booksService.UpdateBookInfo(99L, updatedInfo);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        @DisplayName("Deve retornar Not Found ao tentar apagar um livro inexistente")
        void deleteBook_WhenBookDoesNotExist_ShouldReturnNotFound() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

            ResponseEntity<Void> response = booksService.DeleteBook(99L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

            verify(bookRepository, never()).deleteById(anyLong());
        }
    }
}