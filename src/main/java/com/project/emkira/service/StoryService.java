package com.project.emkira.service;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.model.Story;

import java.util.List;

public interface StoryService {

    Story addStory(StoryRequest storyRequest);

    List<Story> getStoryByEpicId(Long epicId);

    List<Story> getAllStories();

    List<Story> getStoryBySprintId(Long sprintId);

    void updateStatus(Story.Status status, Long id);
}
