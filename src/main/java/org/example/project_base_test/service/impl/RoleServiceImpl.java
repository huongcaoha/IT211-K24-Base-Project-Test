package org.example.project_base_test.service.impl;

import org.example.project_base_test.model.entity.Role;
import org.example.project_base_test.repository.RoleRepository;
import org.example.project_base_test.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findByRoleName(roleName).orElse(null);
    }

    @Override
    public void initializeRole() {
        if(roleRepository.count() == 0){
            List<Role> roles = new ArrayList<>();
            roles.add(Role.builder().roleName("ADMIN").build());
            roles.add(Role.builder().roleName("USER").build());
            roleRepository.saveAll(roles);
        }
    }
}
