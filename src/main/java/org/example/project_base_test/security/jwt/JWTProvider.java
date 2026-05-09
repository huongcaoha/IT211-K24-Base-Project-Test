package org.example.project_base_test.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.project_base_test.model.entity.User;
import org.example.project_base_test.security.principle.UserPrinciple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {
    @Value("${jwt.secret}")
    private String secret;
    // Viết logic tạo access token và validate token ở đây

    public String generateToken(UserPrinciple userPrinciple) {
        String newSecret = secret + userPrinciple.getUser().getSecret();
        Key key = Keys.hmacShaKeyFor(newSecret.getBytes());
        return Jwts
                .builder()
                .subject(userPrinciple.getUsername())
                .issuedAt(new Date())
                .signWith(key)
                .claim("roles", userPrinciple.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + 300 * 1000))
                .compact();
    }

}
