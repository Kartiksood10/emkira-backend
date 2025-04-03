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

    @GetMapping("/{epicId}")
    public List<Story> getStoryByEpicId(@PathVariable Long epicId){

        return storyService.getStoryByEpicId(epicId);
    }

    @GetMapping("/findAll")
    public List<Story> getAllStories(){

        return storyService.getAllStories();
    }
}
