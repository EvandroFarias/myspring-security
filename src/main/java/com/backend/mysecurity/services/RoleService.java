package com.backend.mysecurity.services;

import com.backend.mysecurity.models.RoleModel;
import com.backend.mysecurity.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleModel saveRole(RoleModel role) throws Exception {
        try {

            return roleRepository.save(role);
        } catch (Exception ex){
            throw  ex;
        }
    }
}
