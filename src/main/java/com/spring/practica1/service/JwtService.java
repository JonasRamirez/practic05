package com.spring.practica1.service;

import com.spring.practica1.domain.model.MockEndpoint;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private final Key key;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(MockEndpoint mock) {
        Date expiration = Date.from(
                mock.getExpiresAt()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        return Jwts.builder()
                .setSubject("mock:" + mock.getId())
                .claim("mockId", mock.getId())
                .claim("mockPath", mock.getPath())
                .claim("mockExpiresAt", expiration.getTime())
                .claim("mockName", mock.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getMockIdFromToken(String token) {
        return validateToken(token).get("mockId", Long.class);
    }
}
