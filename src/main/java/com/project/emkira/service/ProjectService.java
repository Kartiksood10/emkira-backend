package com.project.emkira.service;

import com.project.emkira.model.Project;

import java.util.List;

public interface ProjectService {

    Project addProject(Project project);

    List<Project> getAllProjects();

    Project getProjectByName(String projectName);

    String deleteProjectById(Long id);

    Project updateProject(Long id, Project projectDetails);

}
