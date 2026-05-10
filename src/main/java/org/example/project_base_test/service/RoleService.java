package org.example.project_base_test.service;

import org.example.project_base_test.model.entity.Role;

public interface RoleService {
    Role getRole(String roleName);
    void initializeRole();
}
