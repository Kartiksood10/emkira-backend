package com.project.emkira.service.impl;


import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    public ProjectServiceImpl(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @Override
    public Project addProject(Project project) {
        return projectRepo.save(project);
    }

    @Cacheable(value = "Projects")
    @Override
    public List<Project> getAllProjects() {

        logger.info("Fetching all projects from DB");
        return projectRepo.findAll();
    }

    // key is the parameter present in the function which redis uses to store data
    @Cacheable(value = "Project Name", key = "#projectName")
    @Override
    public Project getProjectByName(String projectName) {
        // This will be logged when the data is fetched from the database
        logger.info("Fetching project with name '{}' from the database", projectName);

        // Simulate DB call and return project
        return projectRepo.findByName(projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Project with name '" + projectName + "' not found"));
    }

    @Override
    public String deleteProjectById(Long id) {

        if(projectRepo.existsById(id)) {

            projectRepo.deleteById(id);
            return "Project with id '" + id + "' deleted successfully";
        }

        throw new ProjectNotFoundException("Project with id '" + id + "' not found");
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

    @Override
    public List<String> getProjectByManager(String manager) {
        return projectRepo.findProjectByManager(manager);
    }

    @Override
    public List<String> getProjectByType(Project.Type type) {
        return projectRepo.findProjectByType(type);
    }


}
