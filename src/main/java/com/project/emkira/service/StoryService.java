package com.project.emkira.service;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.model.Story;

public interface StoryService {

    Story addStory(StoryRequest storyRequest);
}
