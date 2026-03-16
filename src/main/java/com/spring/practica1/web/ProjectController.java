package com.spring.practica1.web;

import com.spring.practica1.domain.model.Project;
import com.spring.practica1.domain.model.User;
import com.spring.practica1.domain.repository.MockEndpointRepository;
import com.spring.practica1.domain.repository.ProjectRepository;
import com.spring.practica1.domain.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectController(
            ProjectRepository projectRepo,
            UserRepository userRepo
    ) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String listProjects(Model model, Authentication auth) {

        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Project> projects = isAdmin
                ? projectRepo.findAll()
                : projectRepo.findByOwnerUsername(user.getUsername());

        model.addAttribute("projects", projects);
        model.addAttribute("isAdmin", isAdmin);

        return "projects/list";
    }

    @GetMapping("/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        Project project = projectRepo.findById(id)
                .orElseThrow();

        model.addAttribute("project", project);
        model.addAttribute("mocks", project.getMocks());
        return "projects/detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, Authentication auth) {

        Project project = projectRepo.findById(id)
                .orElseThrow();
        project.getMocks().clear();

        projectRepo.delete(project);

        return "redirect:/projects";
    }
}