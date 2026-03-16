package com.spring.practica1.domain.repository;

import com.spring.practica1.domain.model.Role;
import com.spring.practica1.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}