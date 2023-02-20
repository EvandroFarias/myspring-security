package com.backend.mysecurity.controllers;

import com.backend.mysecurity.models.RoleModel;
import com.backend.mysecurity.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Object> saveRole(@RequestBody RoleModel roleModel, Errors errors){
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.status(400).body(errors.getAllErrors().stream().map(error -> error.getDefaultMessage()));
            }
            return ResponseEntity.status(201).body(roleService.saveRole(roleModel));
        } catch (Exception ex){
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

}
