package com.project.emkira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryRequest {

    private String title;
    private String description;
    private String status;
    private String comment;
    private String assignee;
    private String reporter;
    private int storyPoints;
    private String priority;
    private int estimate;
    private Long projectId;
    private Long assigneeId;
    private Long reporterId;
    private Long epicId;
    private Long sprintId;
}
