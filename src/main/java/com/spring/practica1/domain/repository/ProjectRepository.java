package com.spring.practica1.domain.repository;

import com.spring.practica1.domain.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerUsername(String username);

    @EntityGraph(attributePaths = {
            "owner",
            "mocks",
            "mocks.owner",
            "mocks.headers"
    })
    Optional<Project> findWithMocksById(Long id);

}