package com.backend.mysecurity.controllers;

import com.backend.mysecurity.customExceptions.AlreadyRegisteredException;
import com.backend.mysecurity.customExceptions.RedundancyException;
import com.backend.mysecurity.dtos.user.UserViewDTO;
import com.backend.mysecurity.enums.RoleEnum;
import com.backend.mysecurity.models.UserModel;
import com.backend.mysecurity.services.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserModel user, Errors errors) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.status(400).body(errors.getAllErrors().stream().map(error -> error.getDefaultMessage()));
            }
            user.setPassword((passwordEncoder.encode(user.getPassword())));
            return ResponseEntity.status(201).body(userService.saveUser(user));
        } catch (AlreadyRegisteredException | IllegalArgumentException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping(path = "/activate")
    public ResponseEntity<Object> activateUser(@RequestParam(value = "user") UUID userId){
        try {
            return ResponseEntity.status(200).body(userService.activateUser(userId));
        }
        catch (NoSuchElementException | RedundancyException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/setrole")
    public ResponseEntity<Object> setRole(@RequestParam(value = "user") UUID userId, @RequestBody String roleName){
        try {
            return ResponseEntity.status(200).body(userService.setRole(userId, RoleEnum.valueOf(roleName.strip().toUpperCase())));
        } catch (NoSuchElementException | RedundancyException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/deactivate")
    public ResponseEntity<Object> disableUser(@RequestParam(value = "user") UUID userId){
        try {
            return ResponseEntity.status(200).body(userService.disableUser(userId));
        } catch (RedundancyException | NoSuchElementException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
