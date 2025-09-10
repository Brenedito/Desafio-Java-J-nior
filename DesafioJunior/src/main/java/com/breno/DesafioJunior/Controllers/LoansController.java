package com.breno.DesafioJunior.Controllers;

import com.breno.DesafioJunior.Dtos.LoanDTO;
import com.breno.DesafioJunior.Services.LoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Empréstimos", description="Endpoints para CRUD de empréstimos")
@RestController
@RequestMapping("/api/loans")
public class LoansController {

    @Autowired
    private LoansService loansService;

    @Operation(summary="Listar todos os empréstimos", description="Retorna uma lista de todos os empréstimos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Lista de empréstimos retornada com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode ="404", description = "Empréstimo não encontrado")
    })
    @GetMapping
    public ResponseEntity<List<LoanDTO>> ListAllLoans(@RequestParam(required = false) Long after,@RequestParam(defaultValue = "10") int size) {
        return loansService.ListLoans(after, size);
    }

    @Operation(summary="Buscar empréstimo por ID", description="Retorna os detalhes de um empréstimo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Empréstimo encontrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="404", description="Empréstimo não encontrado")
    })
    @GetMapping("/user/{userid}")
    public ResponseEntity<List<LoanDTO>> FindLoanByUserId(@PathVariable Long userid) {
        return loansService.FindLoanByUserId(userid);
    }

    @Operation(summary="Cadastrar novo empréstimo", description="Cadastra um novo empréstimo ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description="Empréstimo cadastrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="400", description="Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<LoanDTO> RegisterNewLoans(@RequestBody LoanDTO loanDTO) {
        return loansService.RegisterNewLoan(loanDTO);
    }

    @Operation(summary="Atualizar informações do empréstimo", description="Atualiza os detalhes de um empréstimo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Empréstimo atualizado com sucesso"),
            @ApiResponse(responseCode="404", description="Empréstimo não encontrado"),
            @ApiResponse(responseCode="400", description="Requisição inválida")
    })
    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> RegisterLoanReturn(@PathVariable Long id){
        return loansService.RegisterLoanReturn(id);
    }
}
