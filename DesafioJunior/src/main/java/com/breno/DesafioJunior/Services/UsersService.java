package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Models.UserModel;
import com.breno.DesafioJunior.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<UserDTO>> ListUsers(Long after, int size){
        logger.info("Buscando todos os usuários registrados.");

        Pageable pageable = PageRequest.of(0, size);
        List<UserDTO> ListOfUsers;

        if(after == null){
            logger.info("Listando os 10 primeiros usuários.");
            ListOfUsers = userRepository.findByOrderByIdAsc(pageable).stream().map(this::toDTO).toList();
        } else {
            logger.info("Listando os 10 usuários após o ID: {}", after);
            ListOfUsers = userRepository.findByUserIdGreaterThanOrderByIdAsc(after, pageable).stream().map(this::toDTO).toList();
        }
        if(ListOfUsers.isEmpty()){
            logger.info("Nenhum usuário encontrado no sistema.");
            return ResponseEntity.notFound().build();
        }

        logger.info("Encontrados {} usuários no sistema.", ListOfUsers.size());
        return ResponseEntity.ok(ListOfUsers);
    }

    public ResponseEntity<UserDTO> FindUserById(Long id){
        logger.info("Buscando usuário com ID: {}", id);
        UserDTO User = userRepository.findById(id).map(this::toDTO).orElse(null);
        if(User != null){
            logger.info("Usuário com ID: {} encontrado.", id);
            return ResponseEntity.ok(User);
        }
        logger.warn("Usuário com ID: {} não encontrado.", id);
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<UserDTO> RegisterNewUser(UserDTO userDTO){
        logger.info("Registrando novo usuário com os seguintes dados, nome: {} email: {} cpf: {}", userDTO.name(), userDTO.email(), userDTO.cpf());
        UserModel RegisteredUser = new UserModel(
                userDTO.name(),
                userDTO.email(),
                userDTO.cpf(),
                LocalDateTime.now(),
                UserENUM.valueOf(String.valueOf(userDTO.status()))
        );

        userRepository.save(RegisteredUser);
        logger.info("Usuário {} registrado com sucesso.", RegisteredUser);
        return ResponseEntity.ok(toDTO(RegisteredUser));

    }

    public ResponseEntity<UserDTO> UpdateUserInfo(Long id, UserDTO NewUserInfo){
        logger.info("Atualizando informações do usuário com ID: {}", id);
        UserModel OldUserInfo = userRepository.findById(id).orElse(null);

        //É possivel diminuir os if's Usando o Mapper
        if(OldUserInfo == null){
            logger.info("Usuário com ID: {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        }
        if(NewUserInfo.name() != null){
            logger.info("Atualizando nome do usuário com ID: {} para {}", id, NewUserInfo.name());
            OldUserInfo.setName(NewUserInfo.name());
        }
        if(NewUserInfo.email() != null){
            logger.info("Atualizando email do usuário com ID: {} para {}", id, NewUserInfo.email());
            OldUserInfo.setEmail(NewUserInfo.email());
        }
        if(NewUserInfo.cpf() != null){
            logger.info("Atualizando CPF do usuário com ID: {} para {}", id, NewUserInfo.cpf());
            OldUserInfo.setCpf(NewUserInfo.cpf());
        }
        if(NewUserInfo.status() != null){
            logger.info("Atualizando status do usuário com ID: {} para {}", id, NewUserInfo.status());
            OldUserInfo.setStatus(NewUserInfo.status());
        }

        userRepository.save(OldUserInfo);
        logger.info("Informações do usuário com ID: {} atualizadas com sucesso.", id);
        return ResponseEntity.ok(toDTO(OldUserInfo));
    }

    public UserDTO toDTO(UserModel model) {
        return new UserDTO(
                model.getUser_id(),
                model.getName(),
                model.getEmail(),
                model.getCpf(),
                model.getRegistration_date(),
                model.getStatus()
        );
    }

}
