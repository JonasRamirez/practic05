package com.spring.practica1.support;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InstanceHeaderFilter extends OncePerRequestFilter {

    private final InstanceInfoProvider instanceInfoProvider;

    public InstanceHeaderFilter(InstanceInfoProvider instanceInfoProvider) {
        this.instanceInfoProvider = instanceInfoProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-Instance-Id", instanceInfoProvider.getInstanceId());
        response.setHeader("X-Instance-Port", instanceInfoProvider.getPort());
        filterChain.doFilter(request, response);
    }
}
