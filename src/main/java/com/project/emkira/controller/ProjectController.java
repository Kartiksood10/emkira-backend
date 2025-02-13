package com.project.emkira.controller;

import com.project.emkira.model.Project;
import com.project.emkira.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public Project addProject(@RequestBody Project project) {

        return projectService.addProject(project);
    }

    @GetMapping("/getAll")
    public List<Project> getAllProjects() {

        return projectService.getAllProjects();
    }

    @GetMapping("/name/{projectName}")
    public ResponseEntity<Project> getProjectByName(@PathVariable String projectName) {
        Project project = projectService.getProjectByName(projectName);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProjectById(@PathVariable Long id) {
        return projectService.deleteProjectById(id);
    }

    @PutMapping("/update/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {

        return projectService.updateProject(id, project);
    }

}
