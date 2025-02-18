package com.project.emkira.service.impl;

import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.exception.UserEnrolledException;
import com.project.emkira.exception.UserNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.model.ProjectUser;
import com.project.emkira.model.User;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.repo.ProjectUserRepo;
import com.project.emkira.repo.UserRepo;
import com.project.emkira.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ProjectUserRepo projectUserRepo;

    @Autowired
    public ProjectUserServiceImpl(ProjectRepo projectRepo, UserRepo userRepo, ProjectUserRepo projectUserRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.projectUserRepo = projectUserRepo;
    }

    @Override
    public String enrollUserInProject(Long projectId, Long userId, String roleString) {

        // checks if project exists in DB
        Project existingProject = projectRepo.findById(projectId).
                orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // checks if user exists in DB
        User existingUser = userRepo.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user is already enrolled in the project
        // If combination of userId and projectId is present return exception
        Optional<ProjectUser> existingProjectUser = projectUserRepo.findByUserIdAndProjectId(userId, projectId);
        if (existingProjectUser.isPresent()) {
            throw new UserEnrolledException("User already enrolled in this project!");
        }

        // Enroll the user
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(existingProject);
        projectUser.setUser(existingUser);
        projectUser.setRole(ProjectUser.Role.valueOf(roleString.toUpperCase()));

        projectUserRepo.save(projectUser);
        return "User enrolled successfully!";

    }

    @Override
    public List<Long> getUserIdsByProjectId(Long projectId) {

        if(projectRepo.findById(projectId).isEmpty()){

            throw new ProjectNotFoundException("Project not found");
        }

        return projectUserRepo.findAllUserIdsByProjectId(projectId);
    }

    @Override
    public List<String> getAccountNamesByProjectId(Long projectId) {

        if(projectRepo.findById(projectId).isEmpty()){

            throw new ProjectNotFoundException("Project not found");
        }

        return projectUserRepo.findAllAccountNamesByProjectId(projectId);
    }

    @Override
    public Long getUserCountByProjectId(Long projectId) {

        if(projectRepo.findById(projectId).isEmpty()){

            throw new ProjectNotFoundException("Project not found");
        }

        return projectUserRepo.findUserCountByProjectId(projectId);
    }

}

