package org.example.project_base_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializeService {
    @Autowired
    private RoleService roleService;

    public void initializeData() {
        roleService.initializeRole();
    }
}
