package com.project.emkira.controller;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.model.Story;
import com.project.emkira.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
