package org.example.project_base_test.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.checkerframework.checker.units.qual.A;
import org.example.project_base_test.model.entity.User;
import org.example.project_base_test.repository.UserRepository;
import org.example.project_base_test.security.principle.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Autowired
    private UserRepository userRepository;

    public String generateToken(UserPrinciple userPrinciple) {
        return Jwts
                .builder()
                .subject(userPrinciple.getUsername())
                .issuedAt(new Date())
                .signWith(getSigningKey())
                .claim("roles", userPrinciple.getAuthorities())
                .claim("secret", userPrinciple.getUser().getSecret())
                .expiration(new Date(System.currentTimeMillis() + 300 * 1000))
                .compact();
    }

    public String generateRefreshToken(UserPrinciple userPrinciple) {

        return Jwts
                .builder()
                .subject(userPrinciple.getUsername())
                .issuedAt(new Date())
                .claim("secret", userPrinciple.getUser().getSecret())
                .expiration(new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 30))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getPayloadToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }



    public String validateToken(String token) {
        try {
            Claims claims = getPayloadToken(token);
            if (claims != null && claims.get("roles") != null) {
                return "OK";
            }
            return "Token invalid";
        }catch (SignatureException e){
            return "Invalid signature";
        }catch (ExpiredJwtException e){
            return "Expired token";
        }catch (MalformedJwtException e){
            return "Invalid format";
        }catch (IllegalArgumentException e){
            return "Invalid token";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public boolean validateRefreshToken(String token) {
        try {
            return getPayloadToken(token) != null;
        } catch (Exception e) {
            return false;
        }
    }


}
