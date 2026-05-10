package org.example.project_base_test.repository;

import org.example.project_base_test.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
