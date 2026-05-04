package org.example.project_base_test.repository;

import org.example.project_base_test.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUsersByUsername(String username);
}
