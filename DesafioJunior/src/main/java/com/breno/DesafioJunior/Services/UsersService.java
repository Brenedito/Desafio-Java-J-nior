package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Exceptions.DataIntegrityException;
import com.breno.DesafioJunior.Exceptions.ResourceNotFoundException;
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
            ListOfUsers = userRepository.findByOrderByIdAsc(pageable).stream().map(this::toDTO).toList();
            if(ListOfUsers.isEmpty()){
                logger.info("Nenhum usuário encontrado no sistema.");
                throw new ResourceNotFoundException("Nenhum usuário encontrado no sistema.");
            } else {
                logger.info("Listando os 10 primeiros usuários.");
            }
        } else {
            ListOfUsers = userRepository.findByUserIdGreaterThanOrderByIdAsc(after, pageable).stream().map(this::toDTO).toList();
            if(ListOfUsers.isEmpty()){
                logger.info("Nenhum usuário encontrado no sistema.");
                throw new ResourceNotFoundException("Nenhum usuário encontrado apos o ID: " + after);
            }else {
                logger.info("Listando os 10 usuários após o ID: {}", after);
            }
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
        }else {
            logger.warn("Usuário com ID: {} não encontrado.", id);
            throw new ResourceNotFoundException("Nenhum usuário com ID " + id + " cadastrados no sistema.");
        }
    }

    public ResponseEntity<UserDTO> RegisterNewUser(UserDTO userDTO){
        logger.info("Registrando novo usuário com os seguintes dados, nome: {} email: {} cpf: {}", userDTO.name(), userDTO.email(), userDTO.cpf());
        UserModel RegisteredUser = new UserModel(
                userDTO.name(),
                userDTO.email(),
                userDTO.cpf(),
                LocalDateTime.now(),
                UserENUM.ATIVO
        );
        if(userRepository.existsByEmail(userDTO.email())){
            logger.warn("Falha ao registrar usuário: Email {} já está em uso.", userDTO.email());
            throw new DataIntegrityException("Email " + userDTO.email() + " já está em uso.");
        }
        if(userRepository.existsByCpf(userDTO.cpf())){
            logger.warn("Falha ao registrar usuário: CPF {} já está em uso.", userDTO.cpf());
            throw new DataIntegrityException("CPF " + userDTO.cpf() + " já está em uso.");
        }

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
            throw new ResourceNotFoundException("Nenhum usuário com ID " + id + " cadastrados no sistema.");
        }
        if(NewUserInfo.name() != null){
            logger.info("Atualizando nome do usuário com ID: {} para {}", id, NewUserInfo.name());
            OldUserInfo.setName(NewUserInfo.name());
        }
        if(NewUserInfo.email() != null){
            if(userRepository.existsByEmail(NewUserInfo.email())){
                logger.warn("Falha ao atualizar email: Email {} já está em uso.", NewUserInfo.email());
                throw new DataIntegrityException("Email " + NewUserInfo.email() + " já está em uso.");
            }
            logger.info("Atualizando email do usuário com ID: {} para {}", id, NewUserInfo.email());
            OldUserInfo.setEmail(NewUserInfo.email());
        }
        if(NewUserInfo.cpf() != null){
            if(userRepository.existsByCpf(NewUserInfo.cpf())){
                logger.warn("Falha ao atualizar CPF: CPF {} já está em uso.", NewUserInfo.cpf());
                throw new DataIntegrityException("CPF " + NewUserInfo.cpf() + " já está em uso.");
            }
            logger.info("Atualizando CPF do usuário com ID: {} para {}", id, NewUserInfo.cpf());
            OldUserInfo.setCpf(NewUserInfo.cpf()); //CPF não deve ser atualizado em um sistema real
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
