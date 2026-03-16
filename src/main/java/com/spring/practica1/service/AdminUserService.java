package com.spring.practica1.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.practica1.crud.RoleController;
import com.spring.practica1.crud.UserCreateController;
import com.spring.practica1.crud.UserResponseController;
import com.spring.practica1.crud.UserUpdateController;
import com.spring.practica1.domain.model.Role;
import com.spring.practica1.domain.model.User;
import com.spring.practica1.domain.repository.RoleRepository;
import com.spring.practica1.domain.repository.UserRepository;
import com.spring.practica1.exception.DuplicateResourceException;
import com.spring.practica1.exception.ResourceNotFoundException;

@Service
public class AdminUserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public List<UserResponseController> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UserResponseController getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToResponseDTO(user);
    }
    
    @Transactional
    public UserResponseController createUser(UserCreateController createDTO) {
        if (userRepository.findByUsername(createDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + createDTO.getUsername());
        }
        
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        
        if (createDTO.getRoleIds() != null && !createDTO.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : createDTO.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        
        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }
    
    @Transactional
    public UserResponseController updateUser(Long id, UserUpdateController updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isEmpty()) {
            if (!user.getUsername().equals(updateDTO.getUsername()) && 
                userRepository.findByUsername(updateDTO.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Username already exists: " + updateDTO.getUsername());
            }
            user.setUsername(updateDTO.getUsername());
        }
        
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }
        
        if (updateDTO.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : updateDTO.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        
        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<RoleController> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleController(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }
    
    private UserResponseController convertToResponseDTO(User user) {
        UserResponseController dto = new UserResponseController();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        
        if (user.getRoles() != null) {
            Set<RoleController> roleDTOs = user.getRoles().stream()
                    .map(role -> new RoleController(role.getId(), role.getName()))
                    .collect(Collectors.toSet());
            dto.setRoles(roleDTOs);
        }
        
        return dto;
    }
}