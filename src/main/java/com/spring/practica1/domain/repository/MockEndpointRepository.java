package com.spring.practica1.domain.repository;

import com.spring.practica1.domain.model.MockEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MockEndpointRepository extends JpaRepository<MockEndpoint, Long> {
    Optional<MockEndpoint> findByPathAndMethod(String path, String method);

    List<MockEndpoint> findByOwnerUsername(String username);

    Optional<MockEndpoint> findByPathAndMethodAndProjectId(
            String path, String method, Long projectId);
}