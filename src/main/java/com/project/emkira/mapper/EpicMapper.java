package com.project.emkira.mapper;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.model.Epic;
import com.project.emkira.model.Project;
import org.springframework.stereotype.Component;

@Component
public class EpicMapper {

    public Epic toEntity(EpicRequest request, Project project) {
        Epic epic = new Epic();
        epic.setTitle(request.getTitle());
        epic.setDescription(request.getDescription());
        epic.setStatus(Epic.Status.valueOf(request.getStatus())); // Convert String to Enum
        epic.setComment(request.getComment());
        epic.setAssignee(request.getAssignee());
        epic.setReporter(request.getReporter());
        epic.setStory_points(request.getStoryPoints());
        epic.setPriority(Epic.Priority.valueOf(request.getPriority())); // Convert String to Enum
        epic.setEstimate(request.getEstimate());
        epic.setProject(project);

        return epic;
    }
}
