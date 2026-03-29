package com.spring.practica1.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InstanceInfoProvider {

    private final Environment environment;
    private final String instanceId;

    public InstanceInfoProvider(Environment environment,
                                @Value("${INSTANCE_ID:${HOSTNAME:unknown}}") String instanceId) {
        this.environment = environment;
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getPort() {
        String port = environment.getProperty("local.server.port");
        if (port == null || port.isBlank()) {
            port = environment.getProperty("server.port", "8080");
        }
        return port;
    }
}
