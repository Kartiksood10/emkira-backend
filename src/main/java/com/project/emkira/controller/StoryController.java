package com.project.emkira.controller;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.model.Story;
import com.project.emkira.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stories")
public class StoryController {

    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/add")
    public Story addStory(@RequestBody StoryRequest request) {

        return storyService.addStory(request);
    }

    @GetMapping("/{epicId}/epic")
    public List<Story> getStoryByEpicId(@PathVariable Long epicId){

        return storyService.getStoryByEpicId(epicId);
    }

    @GetMapping("/findAll")
    public List<Story> getAllStories(){

        return storyService.getAllStories();
    }

    @GetMapping("/{sprintId}/sprint")
    public List<Story> getStoryBySprintId(@PathVariable Long sprintId){

        return storyService.getStoryBySprintId(sprintId);
    }

    @PutMapping("/{id}/status")
    // Request param eg : http://localhost:8080/api/v1/stories/1/status?status=TO_DO
    // key = status value = TO_DO
    public void updateStatus(@PathVariable Long id, @RequestParam Story.Status status){

        storyService.updateStatus(status, id);
    }

    @GetMapping("/{sprintId}/storyPoints")
    public Long getStoryPointsPerSprint(@PathVariable Long sprintId){

        return storyService.getTotalStoryPointsPerSprint(sprintId);
    }

    @GetMapping("/assignee/priority")
    public List<Story> getHighPriorityStoriesByAssignee(@RequestParam String assignee){

        return storyService.getHighPriorityStoriesByAssignee(assignee);
    }
}
