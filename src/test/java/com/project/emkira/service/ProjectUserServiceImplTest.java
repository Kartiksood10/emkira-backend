package com.project.emkira.service;

import com.project.emkira.exception.UserEnrolledException;
import com.project.emkira.model.Project;
import com.project.emkira.model.ProjectUser;
import com.project.emkira.model.User;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.repo.ProjectUserRepo;
import com.project.emkira.repo.UserRepo;
import com.project.emkira.service.impl.ProjectUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectUserServiceImplTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ProjectUserRepo projectUserRepo;

    @InjectMocks
    private ProjectUserServiceImpl projectUserServiceImpl;

    private ProjectUser projectUser;
    private User user;
    private Project project;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setAccountName("Test123");
        user.setPassword("password");
        user.setEmail("test@email.com");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setType(Project.Type.COMPANY_MANAGED);
        project.setManager("Test Manager");

        projectUser = new ProjectUser();
        projectUser.setId(1L);
        projectUser.setUser(user);
        projectUser.setProject(project);
        projectUser.setRole(ProjectUser.Role.DEVELOPER);
    }

}
