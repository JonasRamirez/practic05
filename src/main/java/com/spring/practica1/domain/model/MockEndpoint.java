package com.spring.practica1.domain.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;


import com.spring.practica1.domain.model.Project;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class MockEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del mock es obligatorio")
    private String name;

    private String description;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del mock es obligatorio")
    private String path;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Integer statusCode;

    @Column(nullable = false)
    private String contentType;

    @Lob
    private String responseBody;

    private int delaySeconds;

    private boolean jwtProtected;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private ExpirationOption expirationOption;

    @ElementCollection
    @CollectionTable(
            name = "mock_headers",
            joinColumns = @JoinColumn(name = "mock_id")
    )
    @MapKeyColumn(name = "header_name")
    @Column(name = "header_value", columnDefinition = "TEXT")
    private Map<String, String> headers = new HashMap<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    private User owner;

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    public int getDelaySeconds() { return delaySeconds; }
    public void setDelaySeconds(int delaySeconds) { this.delaySeconds = delaySeconds; }

    public boolean isJwtProtected() { return jwtProtected; }
    public void setJwtProtected(boolean jwtProtected) { this.jwtProtected = jwtProtected; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public ExpirationOption getExpirationOption() { return expirationOption; }
    public void setExpirationOption(ExpirationOption expirationOption) {
        this.expirationOption = expirationOption;
    }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}