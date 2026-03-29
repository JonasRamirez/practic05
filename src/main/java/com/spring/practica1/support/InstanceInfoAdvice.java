package com.spring.practica1.support;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class InstanceInfoAdvice {

    private final InstanceInfoProvider instanceInfoProvider;

    public InstanceInfoAdvice(InstanceInfoProvider instanceInfoProvider) {
        this.instanceInfoProvider = instanceInfoProvider;
    }

    @ModelAttribute("instanceId")
    public String instanceId() {
        return instanceInfoProvider.getInstanceId();
    }

    @ModelAttribute("instancePort")
    public String instancePort() {
        return instanceInfoProvider.getPort();
    }
}
