package com.project.emkira.service;

import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    // Mock projectRepo so that we do not access database for testing
    @Mock
    private ProjectRepo projectRepo;

    // Injects mocked repo into serviceImpl
    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;

    private Project project;

    // Runs before every test case, initializing test data
    @BeforeEach
    void setUp() {

        project = new Project();
        project.setId(1L);
        project.setManager("Test Manager");
        project.setName("Test Project");
        project.setType(Project.Type.COMPANY_MANAGED);
    }

    @Test
    void testAddProject() {
        // Instead of calling the real database, Mockito intercepts it and returns project.
        // when save function is mocked and called, for any project then return that project
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        // calling function to be tested
        Project savedProject = projectServiceImpl.addProject(project);

        // Assertions
        // we do not assert ID since it is generated automatically in DB
        assertNotNull(savedProject);
        assertEquals("Test Manager", savedProject.getManager());
        assertEquals("Test Project", savedProject.getName());
        assertEquals(Project.Type.COMPANY_MANAGED, savedProject.getType());

        // verify that save() was called exactly once
        verify(projectRepo, times(1)).save(any(Project.class));

    }

    @Test
    void testGetProjectByName(){

        // Mock repo
        // Optional.of since findByName is Optional and may return null
        // To avoid null pointer exception while testing
        when(projectRepo.findByName(project.getName())).thenReturn(Optional.of(project));

        // Call service function
        Project foundProject = projectServiceImpl.getProjectByName(project.getName());

        assertNotNull(foundProject);
        assertEquals(project.getName(), foundProject.getName());
        assertEquals(project.getManager(), foundProject.getManager());
        assertEquals(project.getType(), foundProject.getType());
        // verify findByName is called only once
        verify(projectRepo, times(1)).findByName(project.getName());
    }

    @Test
    void testGetProjectByNameNotFound(){

        // Defining a random project name which mocks not being in DB
        String invalidProjectName = "Invalid Project";

        // Mocking the repo where if the project name is invalidProjectName(not present in DB) then return empty
        when(projectRepo.findByName(invalidProjectName)).thenReturn(Optional.empty());

        // Throw exception when project not found
        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.getProjectByName(invalidProjectName));

        verify(projectRepo, times(1)).findByName(invalidProjectName);
    }
}
