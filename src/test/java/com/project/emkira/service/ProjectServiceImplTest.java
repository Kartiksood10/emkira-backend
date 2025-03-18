package com.project.emkira.service;

import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
