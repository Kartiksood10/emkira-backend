package com.project.emkira.controller;

import com.project.emkira.dto.ProjectUserRequest;
import com.project.emkira.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
