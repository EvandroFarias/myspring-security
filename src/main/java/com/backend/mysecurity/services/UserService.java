package com.backend.mysecurity.services;

import com.backend.mysecurity.customExceptions.AlreadyRegisteredException;
import com.backend.mysecurity.customExceptions.RedundancyException;
import com.backend.mysecurity.dtos.user.UserViewDTO;
import com.backend.mysecurity.enums.RoleEnum;
import com.backend.mysecurity.models.RoleModel;
import com.backend.mysecurity.models.UserModel;
import com.backend.mysecurity.repositories.RoleRepository;
import com.backend.mysecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserViewDTO saveUser(UserModel user) throws Exception {
        try {
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                throw new AlreadyRegisteredException("Email already registered");
            }

            return UserViewDTO.modelToDTO(userRepository.save(user));
        } catch (Exception ex){
            throw ex;
        }
    }

    public UserViewDTO activateUser(UUID userId) throws Exception{

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User does not exists"));
        if(user.getEnabled()){
            throw new RedundancyException("User is already active");
        }

        user.setEnabled(true);
        user.getRoles().add(roleRepository.findByRoleName(RoleEnum.ROLE_USER).orElseThrow(()-> new NoSuchElementException("ROLE NOT CONFIGURED")));

        user.setLastUpdateDate(new Date(System.currentTimeMillis()));
        return UserViewDTO.modelToDTO(userRepository.save(user));
    }

    public UserModel setRole(UUID userId, RoleEnum role) throws Exception {

        try {
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User does not exists"));

            RoleModel roleToSet = roleRepository.findByRoleName(role).orElseThrow(()-> new NoSuchElementException("Role doest not exists"));
            List<RoleModel> userRoles = user.getRoles();
            if(userRoles.contains(roleToSet)){
                throw new RedundancyException("This user already have this role");
            }
            userRoles.add(roleToSet);
            user.setLastUpdateDate(new Date(System.currentTimeMillis()));

            return userRepository.save(user);
        } catch (Exception ex){
            throw ex;
        }
    }

    public UserViewDTO disableUser(UUID userId) throws Exception{
        try {
            UserModel user = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("User not found"));
            if(!user.getEnabled()){
                throw new RedundancyException("User is not enabled");
            }
            user.setEnabled(false);

            return UserViewDTO.modelToDTO(userRepository.save(user));
        } catch (Exception ex){
            throw ex;
        }
    }

    public static boolean patternMatches(String email) {
        String regexPattern = "^(.+)@(\\S+)$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
