package com.spring.practica1.crud;

public class RoleController {
    private Long id;
    private String name;
    
    public RoleController() {}
    
    public RoleController(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}