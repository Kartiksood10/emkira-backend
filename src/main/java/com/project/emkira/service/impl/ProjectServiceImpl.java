package com.project.emkira.service.impl;


import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectServiceImpl(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @Override
    public Project addProject(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    @Override
    public Project getProjectByName(String projectName) {
        return projectRepo.findByName(projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Project with name '" + projectName + "' not found"));
    }

    @Override
    public String deleteProjectById(Long id) {

        if(projectRepo.existsById(id)) {

            projectRepo.deleteById(id);
            return "Project with id '" + id + "' deleted successfully";
        }
        else{
            return "Project with id '" + id + "' not found";
        }

    }

    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project existingProject = projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id '" + id + "' not found"));

        existingProject.setManager(projectDetails.getManager());
        existingProject.setName(projectDetails.getName());
        existingProject.setType(projectDetails.getType());

        return projectRepo.save(existingProject);
    }

}
