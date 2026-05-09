package org.example.project_base_test.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RateLimiter(name = "rate_limit_auth" , fallbackMethod = "fallback")
public class AuthController {
    // viết các http method ở đây


    public ResponseEntity<String> fallback(Throwable ex) {
        return new ResponseEntity<>("Hệ thống đang bận vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS);
    }
}
