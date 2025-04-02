package com.project.emkira.controller;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.dto.EpicStatusRequest;
import com.project.emkira.model.Epic;
import com.project.emkira.service.EpicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/epics")
@Tag(name = "Epic Controller", description = "APIs for managing epics")
@SecurityRequirement(name = "BearerAuth")
public class EpicController {

    private final EpicService epicService;

    @Autowired
    public EpicController(EpicService epicService) {

        this.epicService = epicService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new epic", description = "Adds a new epic")
    public Epic addEpic(@RequestBody EpicRequest epicRequest) {

        return epicService.addEpic(epicRequest);
    }

    @GetMapping("/projects/{projectId}")
    @Operation(summary = "Get epic by ID", description = "Retrieves List of epics by project")
    public List<Epic> getProjectById(@PathVariable Long projectId) {

        return epicService.getProjectById(projectId);
    }

    @PutMapping("/status/{epicId}")
    @Operation(summary = "Update epic status", description = "Updates the status of epic (new, todo etc.)")
    public Epic updateEpicStatus(@PathVariable Long epicId, @RequestBody EpicStatusRequest request) {

        return epicService.updateEpicStatus(epicId, request);
    }

    @DeleteMapping("/delete/{epicId}")
    @Operation(summary = "Delete an epic by ID", description = "Deletion of epic")
    public String deleteEpic(@PathVariable Long epicId) {

        return epicService.deleteEpic(epicId);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get epic by priority", description = "Retrieve epic by priority")
    public List<Epic> getEpicByPriority(@PathVariable Epic.Priority priority) {

        return epicService.getEpicByPriority(priority);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get epic by status", description = "Retrieve epic by status")
    public List<Epic> getEpicByStatus(@PathVariable Epic.Status status) {

        return epicService.getEpicByStatus(status);
    }
}
