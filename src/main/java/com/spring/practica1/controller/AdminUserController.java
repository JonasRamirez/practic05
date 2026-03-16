package com.spring.practica1.controller;

import com.spring.practica1.crud.*;
import com.spring.practica1.exception.DuplicateResourceException;
import com.spring.practica1.exception.ResourceNotFoundException;
import com.spring.practica1.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @GetMapping
    public ResponseEntity<List<UserResponseController>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseController> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }
    
    @PostMapping
    public ResponseEntity<UserResponseController> createUser(@Valid @RequestBody UserCreateController createDTO) {
        UserResponseController createdUser = adminUserService.createUser(createDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseController> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateController updateDTO) {
        UserResponseController updatedUser = adminUserService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/roles")
    public ResponseEntity<List<RoleController>> getAllRoles() {
        return ResponseEntity.ok(adminUserService.getAllRoles());
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResource(DuplicateResourceException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}