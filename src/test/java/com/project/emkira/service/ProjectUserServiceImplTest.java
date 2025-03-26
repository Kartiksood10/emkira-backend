package com.project.emkira.service;

import com.project.emkira.exception.ProjectNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
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

    @Test
    void testEnrollUserInProject(){

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        // Ensure the user is NOT already enrolled
        when(projectUserRepo.findByUserIdAndProjectId(user.getId(), project.getId())).thenReturn(Optional.empty());

        when(projectUserRepo.save(any(ProjectUser.class))).thenReturn(projectUser);

        String result = projectUserServiceImpl.enrollUserInProject(project.getId(), user.getId(), "DEVELOPER");

        assertNotNull(result);
        assertEquals("User enrolled successfully!", result);

        verify(projectRepo, times(1)).findById(project.getId());
        verify(userRepo, times(1)).findById(user.getId());
        verify(projectUserRepo, times(1)).findByUserIdAndProjectId(user.getId(), project.getId());
        verify(projectUserRepo, times(1)).save(any(ProjectUser.class));

    }

    @Test
    void testUserEnrolledInProject(){

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        when(projectUserRepo.findByUserIdAndProjectId(user.getId(), project.getId())).thenReturn(Optional.of(projectUser));

        assertThrows(UserEnrolledException.class, () -> projectUserServiceImpl.enrollUserInProject(project.getId(), user.getId(), "DEVELOPER"));

        verify(projectRepo, times(1)).findById(project.getId());
        verify(userRepo, times(1)).findById(user.getId());
        verify(projectUserRepo, times(1)).findByUserIdAndProjectId(user.getId(), project.getId());
        verify(projectUserRepo, never()).save(any(ProjectUser.class));

    }

    @Test
    void testGetUserIdsByProjectId(){

        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        userIds.add(2L);

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));

        when(projectUserRepo.findAllUserIdsByProjectId(project.getId())).thenReturn(userIds);

        List<Long> result = projectUserServiceImpl.getUserIdsByProjectId(project.getId());

        assertNotNull(result);
        assertIterableEquals(userIds, result);

        verify(projectRepo, times(1)).findById(project.getId());
        verify(projectUserRepo, times(1)).findAllUserIdsByProjectId(project.getId());
    }

    @Test
    void testProjectNotFound(){

        when(projectRepo.findById(project.getId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, ()-> projectUserServiceImpl.getUserIdsByProjectId(project.getId()));

        verify(projectRepo, times(1)).findById(project.getId());
        verify(projectUserRepo, never()).findAllUserIdsByProjectId(project.getId());
    }

    @Test
    void testGetAccountNameByProjectId(){

        // better than creating new arrayList<>() since this is more readable and list is immutable
        List<String> accountNames = List.of("User1", "User2");

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));

        when(projectUserRepo.findAllAccountNamesByProjectId(project.getId())).thenReturn(accountNames);

        List<String> result = projectUserServiceImpl.getAccountNamesByProjectId(project.getId());

        assertNotNull(result);
        assertIterableEquals(accountNames, result);

        verify(projectRepo, times(1)).findById(project.getId());
        verify(projectUserRepo, times(1)).findAllAccountNamesByProjectId(project.getId());
    }

}
