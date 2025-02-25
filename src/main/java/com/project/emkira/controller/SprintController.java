package com.project.emkira.controller;

import com.project.emkira.model.Sprint;
import com.project.emkira.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintController {

    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping("/{projectId}/add")
    public Sprint addSprint(@RequestBody Sprint sprint, @PathVariable Long projectId) {

        return sprintService.addSprint(sprint, projectId);
    }

    @GetMapping("/{projectId}/sprintNames")
    public List<String> getSprintListByProjectId(@PathVariable Long projectId) {

        return sprintService.getSprintListByProjectId(projectId);
    }
}
