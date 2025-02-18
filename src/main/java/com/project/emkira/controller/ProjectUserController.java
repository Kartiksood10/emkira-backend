package com.project.emkira.controller;

import com.project.emkira.dto.ProjectUserRequest;
import com.project.emkira.model.ProjectUser;
import com.project.emkira.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/project-user")
public class ProjectUserController {

    private final ProjectUserService projectUserService;

    @Autowired
    public ProjectUserController(ProjectUserService projectUserService) {
        this.projectUserService = projectUserService;
    }

    @PostMapping("/enrollUser")
    public String enrollUserInProject(@RequestBody ProjectUserRequest request) {
        return projectUserService.enrollUserInProject(request.getProjectId(), request.getUserId(), request.getRole().name());
    }

    @GetMapping("/{projectId}/userID")
    public List<Long> getUsersByProjectId(@PathVariable("projectId") Long projectId) {

        return projectUserService.getUserIdsByProjectId(projectId);
    }

    @GetMapping("{projectId}/usernames")
    public List<String> getUsernamesByProjectId(@PathVariable("projectId") Long projectId) {

        return projectUserService.getAccountNamesByProjectId(projectId);
    }
}
