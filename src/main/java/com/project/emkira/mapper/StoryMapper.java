package com.project.emkira.mapper;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.model.Epic;
import com.project.emkira.model.Sprint;
import com.project.emkira.model.Story;
import org.springframework.stereotype.Component;

// This class is injected in StoryServiceImpl hence Component annotation
@Component
public class StoryMapper {

    public Story toEntity(StoryRequest request, Epic epic, Sprint sprint) {
        Story story = new Story();
        story.setTitle(request.getTitle());
        story.setDescription(request.getDescription());
        story.setStatus(Story.Status.valueOf(request.getStatus()));
        story.setComment(request.getComment());
        story.setAssignee(request.getAssignee());
        story.setReporter(request.getReporter());
        story.setStory_points(request.getStoryPoints());
        story.setPriority(Story.Priority.valueOf(request.getPriority()));
        story.setEstimate(request.getEstimate());

        story.setSprint(sprint);
        story.setEpic(epic);

        return story;
    }
}
