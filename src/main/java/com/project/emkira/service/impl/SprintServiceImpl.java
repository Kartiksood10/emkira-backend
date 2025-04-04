package com.project.emkira.service.impl;

import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.model.Sprint;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.repo.SprintRepo;
import com.project.emkira.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SprintServiceImpl implements SprintService {

    private final SprintRepo sprintRepo;
    private final ProjectRepo projectRepo;

    @Autowired
    public SprintServiceImpl(SprintRepo sprintRepo, ProjectRepo projectRepo) {

        this.sprintRepo = sprintRepo;
        this.projectRepo = projectRepo;
    }

    @Override
    public Sprint addSprint(Sprint sprint, Long projectId) {

        if(projectRepo.findById(projectId).isEmpty()){

            throw new ProjectNotFoundException("Project not found");
        }

        Optional<Project> project = projectRepo.findById(projectId);

        if(project.isPresent()){
            sprint.setProject(project.get());
        }

        return sprintRepo.save(sprint);
    }

    @Override
    public List<String> getSprintListByProjectId(Long projectId) {

        if(projectRepo.findById(projectId).isEmpty()){

            throw new ProjectNotFoundException("Project not found");
        }
        return sprintRepo.findSprintNamesByProjectId(projectId);
    }
}
