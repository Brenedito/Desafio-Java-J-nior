package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.UserDTO;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Models.UserModel;
import com.breno.DesafioJunior.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<UserDTO>> ListAllUsers(){
        List<UserDTO> AllUsers = userRepository.findAll().stream().map(this::toDTO).toList();
        if(AllUsers.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AllUsers);
    }

    public ResponseEntity<UserDTO> FindUserById(Long id){
        UserDTO User = userRepository.findById(id).map(this::toDTO).orElse(null);
        return ResponseEntity.ok(User);
    }

    public ResponseEntity<UserDTO> RegisterNewUser(UserDTO userDTO){

        UserModel RegisteredUser = new UserModel(
                userDTO.name(),
                userDTO.email(),
                userDTO.cpf(),
                LocalDateTime.now(),
                UserENUM.valueOf(String.valueOf(userDTO.status()))
        );
        userRepository.save(RegisteredUser);

        //Adicionar Verificações de validação e tratamento de erros
        return ResponseEntity.ok(toDTO(RegisteredUser));

    }

    public ResponseEntity<UserDTO> UpdateUserInfo(Long id, UserDTO NewUserInfo){
        UserModel OldUserInfo = userRepository.findById(id).orElse(null);

        //É possivel diminuir os if's Usando o Mapper
        if(OldUserInfo == null){
            return ResponseEntity.notFound().build();
        }
        if(NewUserInfo.name() != null){
            OldUserInfo.setName(NewUserInfo.name());
        }
        if(NewUserInfo.email() != null){
            OldUserInfo.setEmail(NewUserInfo.email());
        }
        if(NewUserInfo.cpf() != null){
            OldUserInfo.setCpf(NewUserInfo.cpf());
        }
        if(NewUserInfo.status() != null){
            OldUserInfo.setStatus(NewUserInfo.status());
        }

        userRepository.save(OldUserInfo);
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
