package com.project.emkira.controller;

import com.project.emkira.dto.ProjectUserRequest;
import com.project.emkira.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{projectId}/userCount")
    public Long getUserCountByProjectId(@PathVariable("projectId") Long projectId) {

        return projectUserService.getUserCountByProjectId(projectId);
    }
}
