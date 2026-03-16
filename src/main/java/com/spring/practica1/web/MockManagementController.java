package com.spring.practica1.web;

import com.spring.practica1.domain.model.MockEndpoint;
import com.spring.practica1.domain.model.Project;
import com.spring.practica1.domain.model.User;
import com.spring.practica1.domain.repository.MockEndpointRepository;
import com.spring.practica1.domain.repository.ProjectRepository;
import com.spring.practica1.domain.repository.UserRepository;
import com.spring.practica1.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mocks")
public class MockManagementController {

    private final MockEndpointRepository mockRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final JwtService jwtService;


    public MockManagementController(
            MockEndpointRepository mockRepo,
            ProjectRepository projectRepo,
            UserRepository userRepo,
            JwtService jwtService
    ) {
        this.mockRepo = mockRepo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @GetMapping("/new")
    public String createForm(Model model, Authentication auth) {

        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        List<Project> projects =
                projectRepo.findByOwnerUsername(user.getUsername());

        MockEndpoint mock = new MockEndpoint();
        mock.setStatusCode(200);
        mock.setContentType("application/json");

        model.addAttribute("mock", mock);
        model.addAttribute("projects", projects);

        return "mocks/form";
    }

    @PostMapping
    public String saveMock(
            @Valid @ModelAttribute("mock") MockEndpoint mock,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String newProjectName,
            @RequestParam(required = false) String newProjectDescription,
            Authentication auth
    ) {

        if (mock.getName() == null || mock.getName().isBlank()
                || mock.getPath() == null || mock.getPath().isBlank()) {
            throw new IllegalStateException("El mock debe tener nombre y path");
        }

        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        Project project;

        if (projectId != null) {
            project = projectRepo.findById(projectId)
                    .orElseThrow();
        } else if (newProjectName != null && !newProjectName.isBlank()) {
            project = new Project();
            project.setName(newProjectName);
            project.setDescription(newProjectDescription);
            project.setOwner(user);
            project = projectRepo.save(project);
        } else {
            throw new IllegalStateException(
                    "Debe seleccionar o crear un proyecto"
            );
        }

        mock.setOwner(user);
        mock.setProject(project);

        applyExpiration(mock);

        String rawPath = mock.getPath().trim();

        if (rawPath.contains("/")) {
            rawPath = rawPath.substring(rawPath.lastIndexOf("/") + 1);
        }

        String finalPath = "/"
                + user.getUsername()
                + "/mock/"
                + project.getId()
                + "/"
                + rawPath;

        mock.setPath(finalPath);

        mock = mockRepo.save(mock);

        if (mock.isJwtProtected() && mock.getExpiresAt() != null) {

            String token = jwtService.generateToken(mock);

            mock.getHeaders().put("X-Mock-Token", token);
            mock.getHeaders().put("X-Mock-Token-Original", token);

            mockRepo.save(mock);
        }

        return "redirect:/projects";
    }


    private void applyExpiration(MockEndpoint mock) {
        if (mock.getExpirationOption() == null) return;

        LocalDateTime now = LocalDateTime.now();

        switch (mock.getExpirationOption()) {
            case ONE_HOUR -> mock.setExpiresAt(now.plusHours(1));
            case ONE_DAY -> mock.setExpiresAt(now.plusDays(1));
            case ONE_WEEK -> mock.setExpiresAt(now.plusWeeks(1));
            case ONE_MONTH -> mock.setExpiresAt(now.plusMonths(1));
            case ONE_YEAR -> mock.setExpiresAt(now.plusYears(1));
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteMock(@PathVariable Long id, Authentication auth) {

        MockEndpoint mock = mockRepo.findById(id)
                .orElseThrow();

        mockRepo.delete(mock);

        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            Authentication auth
    ) {
        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        MockEndpoint mock = mockRepo.findById(id)
                .orElseThrow();

        List<Project> projects =
                projectRepo.findByOwnerUsername(user.getUsername());

        model.addAttribute("mock", mock);
        model.addAttribute("projects", projects);
        model.addAttribute("editMode", true);

        return "mocks/form";
    }

    @PostMapping("/{id}")
    public String updateMock(
            @PathVariable Long id,
            @Valid @ModelAttribute("mock") MockEndpoint formMock,
            Authentication auth
    ) {
        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow();

        MockEndpoint mock = mockRepo.findById(id)
                .orElseThrow();

        if (!mock.getOwner().getId().equals(user.getId())) {
            throw new IllegalStateException("No puedes modificar este mock");
        }

        mock.setName(formMock.getName());
        mock.setMethod(formMock.getMethod());
        mock.setStatusCode(formMock.getStatusCode());
        mock.setContentType(formMock.getContentType());
        mock.setDelaySeconds(formMock.getDelaySeconds());
        mock.setResponseBody(formMock.getResponseBody());
        mock.setDescription(formMock.getDescription());
        mock.setJwtProtected(formMock.isJwtProtected());
        mock.setExpirationOption(formMock.getExpirationOption());
        mock.setHeaders(formMock.getHeaders());

        String rawPath = formMock.getPath().trim();

        if (rawPath.contains("/")) {
            rawPath = rawPath.substring(rawPath.lastIndexOf("/") + 1);
        }

        if (rawPath.isBlank()) {
            throw new IllegalStateException("El path no puede estar vacío");
        }

        String finalPath = "/"
                + user.getUsername()
                + "/mock/"
                + mock.getProject().getId()
                + "/"
                + rawPath;

        mock.setPath(finalPath);

        applyExpiration(mock);

        mock.getHeaders().remove("X-Mock-Token");
        mock.getHeaders().remove("X-Mock-Token-Original");

        if (mock.isJwtProtected() && mock.getExpiresAt() != null) {
            String token = jwtService.generateToken(mock);
            mock.getHeaders().put("X-Mock-Token", token);
            mock.getHeaders().put("X-Mock-Token-Original", token);
        }

        mockRepo.save(mock);

        return "redirect:/projects";
    }

}