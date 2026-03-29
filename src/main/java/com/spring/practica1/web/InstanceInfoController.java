package com.spring.practica1.web;

import com.spring.practica1.support.InstanceInfoProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/instance")
public class InstanceInfoController {

    private final InstanceInfoProvider instanceInfoProvider;

    public InstanceInfoController(InstanceInfoProvider instanceInfoProvider) {
        this.instanceInfoProvider = instanceInfoProvider;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> instanceInfo() {
        return ResponseEntity.ok(
                Map.of(
                        "instanceId", instanceInfoProvider.getInstanceId(),
                        "port", instanceInfoProvider.getPort()
                )
        );
    }
}
