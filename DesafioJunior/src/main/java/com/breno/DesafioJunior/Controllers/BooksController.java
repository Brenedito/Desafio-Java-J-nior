package com.breno.DesafioJunior.Controllers;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Services.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="Livros", description="Endpoints para CRUD de livros")
@RestController
@RequestMapping("/api/books")
public class BooksController {

    @Autowired
    private BooksService bookService;

    @Operation(summary="Listar todos os livros", description="Retorna uma lista de todos os livros cadastrados")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Lista de livros retornada com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Nenhum livro encontrado")
    })
    @GetMapping
    public ResponseEntity<List<BookDTO>> ListAllBooks() {
        return bookService.ListAllBooks();
    }

    @Operation(summary="Buscar livro por ID", description="Retorna os detalhes de um livro específico")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Livro encontrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="404", description="Livro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<BookDTO>> FindBookById(@PathVariable Long id) {
        return bookService.FindBookById(id);
    }

    @Operation(summary="Cadastrar novo livro", description="Cadastra um novo livro ao sistema")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="Livro cadastrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="404", description="Livro não encontrado")
    })
    @PostMapping
    public ResponseEntity<BookDTO> RegisterNewBook(@Valid @RequestBody BookDTO bookDTO) {
        return bookService.RegisterNewBook(bookDTO);
    }

    @Operation(summary="Atualizar informações do livro", description="Atualiza os detalhes de um livro existente")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="Livro atualizado com sucesso"),
            @ApiResponse(responseCode="404", description="Livro não encontrado"),
            @ApiResponse(responseCode="400", description="Requisição inválida")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> UpdateBookInfo(@PathVariable Long id, @RequestBody BookDTO NewBookInfo) {
        return bookService.UpdateBookInfo(id, NewBookInfo);

    }

    @Operation(summary="Deletar livro", description="Remove um livro do sistema pelo seu ID")
    @ApiResponses(value={
            @ApiResponse(responseCode="204", description="Livro deletado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="404", description="Livro não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteBook(@PathVariable Long id) {
        return bookService.DeleteBook(id);
    }


}
