package com.spring.practica1.crud;

import java.util.Set;

public class UserResponseController {
    private Long id;
    private String username;
    private Set<RoleController> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RoleController> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleController> roles) {
        this.roles = roles;
    }
}