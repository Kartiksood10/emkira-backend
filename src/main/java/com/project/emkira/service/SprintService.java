package com.project.emkira.service;

import com.project.emkira.model.Sprint;

import java.util.List;

public interface SprintService {

    Sprint addSprint(Sprint sprint, Long projectId);

    List<String> getSprintListByProjectId(Long projectId);
}
