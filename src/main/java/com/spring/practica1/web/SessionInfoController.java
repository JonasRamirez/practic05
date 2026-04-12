package com.spring.practica1.web;

import com.spring.practica1.support.InstanceInfoProvider;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/session-info")
public class SessionInfoController {

    private final InstanceInfoProvider instanceInfoProvider;

    public SessionInfoController(InstanceInfoProvider instanceInfoProvider) {
        this.instanceInfoProvider = instanceInfoProvider;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> sessionInfo(Authentication authentication, HttpSession session) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("instanceId", instanceInfoProvider.getInstanceId());
        payload.put("port", instanceInfoProvider.getPort());
        payload.put("sessionId", session.getId());
        payload.put("username", authentication.getName());
        return ResponseEntity.ok(payload);
    }
}
