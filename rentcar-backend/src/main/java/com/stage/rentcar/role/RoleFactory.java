package com.stage.rentcar.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;

@Component
public class RoleFactory {
    @Autowired
    RoleRepository roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        switch (role) {
            case "admin" -> {
                return roleRepository.findByName(ERole.ROLE_ADMIN);
            }
            case "client" -> {
                return roleRepository.findByName(ERole.ROLE_CLIENT);
            }
            case "chef_agence" -> {
                return roleRepository.findByName(ERole.ROLE_CHEF_AGENCE);
            }
            default -> throw  new RoleNotFoundException("role inexistant " +  role);
        }
    }
}
