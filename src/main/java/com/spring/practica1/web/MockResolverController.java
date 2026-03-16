package com.spring.practica1.web;

import com.spring.practica1.domain.model.MockEndpoint;
import com.spring.practica1.domain.repository.MockEndpointRepository;
import com.spring.practica1.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("/{username}/mock")
@RestController
public class MockResolverController {

    private final MockEndpointRepository mockRepo;
    private final JwtService jwtService;

    public MockResolverController(MockEndpointRepository mockRepo, JwtService jwtService) {
        this.mockRepo = mockRepo;
        this.jwtService = jwtService;
    }

    @RequestMapping("/**")
    public ResponseEntity<?> resolveMock(HttpServletRequest request)
            throws InterruptedException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        Optional<MockEndpoint> mockOpt =
                mockRepo.findByPathAndMethod(path, method);

        if (mockOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MockEndpoint mock = mockOpt.get();

        if (mock.getExpiresAt() != null &&
                mock.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity
                    .status(410)
                    .header("X-Reason", "Mock expired")
                    .header("X-Expired-At", mock.getExpiresAt().toString())
                    .build();
        }

        if (mock.isJwtProtected()) {
            String auth = request.getHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(401)
                        .header("WWW-Authenticate", "Bearer")
                        .build();
            }

            String token = auth.substring(7);

            try {
                var claims = jwtService.validateToken(token);

                Long mockIdFromToken = claims.get("mockId", Long.class);
                if (!mockIdFromToken.equals(mock.getId())) {
                    return ResponseEntity
                            .status(403)
                            .header("X-Reason", "Token not valid for this mock")
                            .build();
                }

                String mockPathFromToken = claims.get("mockPath", String.class);
                if (!mockPathFromToken.equals(mock.getPath())) {
                    return ResponseEntity
                            .status(403)
                            .header("X-Reason", "Token path mismatch")
                            .build();
                }

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                return ResponseEntity
                        .status(401)
                        .header("X-Reason", "Token expired")
                        .header("X-Expired-At", e.getClaims().getExpiration().toString())
                        .body("El token JWT ha expirado. El mock expiró en: " +
                                e.getClaims().getExpiration());

            } catch (io.jsonwebtoken.SignatureException e) {
                return ResponseEntity
                        .status(401)
                        .header("X-Reason", "Invalid signature")
                        .body("Firma del token inválida");

            } catch (Exception e) {
                return ResponseEntity
                        .status(401)
                        .header("X-Reason", "Invalid token")
                        .body("Token inválido: " + e.getMessage());
            }
        }

        if (mock.getDelaySeconds() > 0) {
            Thread.sleep(mock.getDelaySeconds() * 1000L);
        }

        ResponseEntity.BodyBuilder builder =
                ResponseEntity.status(mock.getStatusCode());

        mock.getHeaders().forEach(builder::header);

        if (mock.getExpiresAt() != null) {
            builder.header("X-Mock-Expires-At", mock.getExpiresAt().toString());
        }

        return builder
                .contentType(MediaType.parseMediaType(mock.getContentType()))
                .body(mock.getResponseBody());
    }
}