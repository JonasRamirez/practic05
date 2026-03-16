package com.spring.practica1.service;

import com.spring.practica1.domain.model.Project;
import com.spring.practica1.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;

    public ProjectService(ProjectRepository projectRepo) {
        this.projectRepo = projectRepo;
    }

    public Project getProjectWithMocks(Long id) {
        return projectRepo.findWithMocksById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }
}
