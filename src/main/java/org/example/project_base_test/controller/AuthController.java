package org.example.project_base_test.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.example.project_base_test.model.dto.SocialLoginRequest;
import org.example.project_base_test.model.entity.User;
import org.example.project_base_test.service.impl.AuthServiceImpl;
import org.example.project_base_test.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RateLimiter(name = "rate_limit_auth" , fallbackMethod = "fallback")
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody SocialLoginRequest request) {

    }

    public ResponseEntity<String> fallback(Throwable ex) {
        return new ResponseEntity<>("Hệ thống đang bận vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS);
    }
}
