package com.breno.DesafioJunior.Controllers;

import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Services.UsersService;
import com.breno.DesafioJunior.Utils.ValidationGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Usuários", description="Endpoints para CRUD de usuários")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Operation(summary="Listar todos os usuários", description="Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> ListAllUsers(@RequestParam(required = false) Long after,@RequestParam(defaultValue = "10") int size) {
        return usersService.ListUsers(after, size);
    }

    @Operation(summary="Buscar usuário por ID", description="Retorna os detalhes de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Usuário encontrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida"),
            @ApiResponse(responseCode="404", description="Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> FindUserById(@PathVariable Long id) {
        return usersService.FindUserById(id);
    }

    @Operation(summary="Cadastrar novo usuário", description="Cadastra um novo usuário ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description="Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode="400", description="Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<UserDTO> RegisterNewUser(@Validated(ValidationGroups.OnCreate.class) @RequestBody UserDTO userDTO) {
        return usersService.RegisterNewUser(userDTO);
    }

    @Operation(summary = "Atualizar informações do usuário", description = "Atualiza os detalhes de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Usuário atualizado com sucesso"),
            @ApiResponse(responseCode="404", description="Usuário não encontrado"),
            @ApiResponse(responseCode="400", description="Requisição inválida")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> UpdateUserInfo(@PathVariable Long id, @Validated(ValidationGroups.OnUpdate.class) @RequestBody UserDTO user){
        return usersService.UpdateUserInfo(id, user);
    }
}
