package com.project.emkira.controller;

import com.project.emkira.model.Project;
import com.project.emkira.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project Controller", description = "APIs for managing projects")
@SecurityRequirement(name = "BearerAuth") // Requires JWT for all APIs in this controller
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new project", description = "Creates a new project in the system.")
    public Project addProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects.")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/name/{projectName}")
    @Operation(summary = "Get project by name", description = "Fetches a project using its name.")
    public ResponseEntity<Project> getProjectByName(@PathVariable String projectName) {
        Project project = projectService.getProjectByName(projectName);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete project by ID", description = "Deletes a project by its ID.")
    public String deleteProjectById(@PathVariable Long id) {
        return projectService.deleteProjectById(id);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project.")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @GetMapping("/manager/{managerName}")
    @Operation(summary = "Get projects by manager", description = "Retrieves projects managed by manager.")
    public List<String> getProjectsByManager(@PathVariable String managerName) {
        return projectService.getProjectByManager(managerName);
    }

    @GetMapping("/type/{typeName}")
    @Operation(summary = "Get projects by type", description = "Retrieves projects by their type.")
    public List<String> getProjectsByType(@PathVariable Project.Type typeName) {
        return projectService.getProjectByType(typeName);
    }
}
