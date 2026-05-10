package org.example.project_base_test.service;

import org.example.project_base_test.model.dto.SocialLoginRequest;
import org.example.project_base_test.model.dto.UserLoginDTO;
import org.example.project_base_test.model.dto.UserLoginResponse;
import org.example.project_base_test.model.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User findByEmail(String email);
    String save(User user);
    ResponseEntity<?> loginGoogle(SocialLoginRequest loginRequest);
    ResponseEntity<?> login(UserLoginDTO userLoginDTO);
}
