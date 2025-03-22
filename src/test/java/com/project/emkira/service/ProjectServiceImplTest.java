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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Writing Unit test cases 3 steps:
// Arrange (mock behavior) -> Mocking repository functions using when().thenReturn()
// Act (call method) -> Service method is called
// Assert (verify output or exception) -> assert and verify is applied

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

    @Test
    void testDeleteProjectById(){

        // check if project exists and return true since it is inside if condition in service method
        when(projectRepo.existsById(1L)).thenReturn(true);

        String result = projectServiceImpl.deleteProjectById(1L);

        assertEquals("Project with id '" + 1L + "' deleted successfully", result);

        verify(projectRepo, times(1)).existsById(1L);
    }

    @Test
    void testDeleteProjectByIdNotFound(){

        Long invalidProjectId = 999L;

        when(projectRepo.existsById(invalidProjectId)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.deleteProjectById(invalidProjectId));

        verify(projectRepo, never()).deleteById(invalidProjectId);
    }

    @Test
    void testUpdateProject(){

        Long projectId = 1L;
        // arraylist for list of sprints, epics, and projectUsers defined in the model class
        Project existingProject = new Project(projectId, "Old Name", Project.Type.COMPANY_MANAGED, "Old Manager", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Project updatedDetails = new Project(projectId, "New Name", Project.Type.TEAM_MANAGED, "New Manager", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // get existing project when finding by id
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(existingProject));
        // when save is used, set updated project details
        when(projectRepo.save(any(Project.class))).thenReturn(updatedDetails);

        Project result = projectServiceImpl.updateProject(projectId, updatedDetails);

        assertNotNull(result);
        assertEquals(updatedDetails.getName(), result.getName());
        assertEquals(updatedDetails.getManager(), result.getManager());
        assertEquals(updatedDetails.getType(), result.getType());

        verify(projectRepo, times(1)).findById(projectId);
        verify(projectRepo, times(1)).save(any(Project.class));
    }

    @Test
    void testUpdateProjectNotFound(){

        Long invalidProjectId = 999L;
        Project updatedDetails = new Project(invalidProjectId, "Updated Name", Project.Type.TEAM_MANAGED,
                "Updated Manager", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(projectRepo.findById(invalidProjectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.updateProject(invalidProjectId, updatedDetails));

        verify(projectRepo, never()).save(any(Project.class));
    }

    @Test
    void testGetProjectByManager(){

        String manager = "Manager";
        List<String> projects = new ArrayList<>();
        projects.add("Project 1");
        projects.add("Project 2");

        when(projectRepo.findProjectByManager(manager)).thenReturn(projects);

        List<String> result = projectServiceImpl.getProjectByManager(manager);

        assertNotNull(result);
        // assertEquals would compare both list objects, assertIterable checks both list elements
        // if order of elements in list are different then assertEquals fails but assertIterable passes
        assertIterableEquals(projects, result);

        verify(projectRepo, times(1)).findProjectByManager(manager);
    }

    @Test
    void testGetProjectByCompanyManagedType(){

        List<String> projects = new ArrayList<>();
        projects.add("project 1");
        projects.add("project 2");

        when(projectRepo.findProjectByType(Project.Type.COMPANY_MANAGED)).thenReturn(projects);

        List<String> result = projectServiceImpl.getProjectByType(Project.Type.COMPANY_MANAGED);

        assertNotNull(result);
        assertIterableEquals(projects, result);

        verify(projectRepo, times(1)).findProjectByType(Project.Type.COMPANY_MANAGED);

    }

    @Test
    void testGetProjectByTeamManagedType(){

        List<String> projects = new ArrayList<>();
        projects.add("project 1");
        projects.add("project 2");

        when(projectRepo.findProjectByType(Project.Type.TEAM_MANAGED)).thenReturn(projects);

        List<String> result = projectServiceImpl.getProjectByType(Project.Type.TEAM_MANAGED);

        assertNotNull(result);
        assertIterableEquals(projects, result);

        verify(projectRepo, times(1)).findProjectByType(Project.Type.TEAM_MANAGED);

    }
}
