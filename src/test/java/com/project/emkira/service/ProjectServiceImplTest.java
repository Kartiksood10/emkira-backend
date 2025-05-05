package com.project.emkira.service;

import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.impl.ProjectServiceImpl;
import com.project.emkira.util.RedisUtil;
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

// @Mock all DB and other util calls used in serviceImpl
// @InjectMocks into the serviceImpl

// Writing Unit test cases 3 steps:
// Arrange (mock behavior) -> Mocking repository functions using when().thenReturn()
// Act (call method) -> Service method is called
// Assert (verify output or exception) -> assert and verify is applied

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    // Mock projectRepo so that we do not access database for testing
    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private RedisUtil redisUtil;

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

        verify(redisUtil, times(1)).delete("all_projects");
        verify(projectRepo, times(1)).save(any(Project.class));
    }

    @Test
    void testGetAllProjects_CacheHit() {
        List<Project> cachedProjects = List.of(project);
        when(redisUtil.get("all_projects")).thenReturn(cachedProjects);

        List<Project> result = projectServiceImpl.getAllProjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(redisUtil, times(1)).get("all_projects");
        verify(projectRepo, never()).findAll();
    }

    @Test
    void testGetAllProjects_CacheMiss() {
        List<Project> dbProjects = List.of(project);
        when(redisUtil.get("all_projects")).thenReturn(null);
        when(projectRepo.findAll()).thenReturn(dbProjects);

        List<Project> result = projectServiceImpl.getAllProjects();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(redisUtil, times(1)).get("all_projects");
        verify(projectRepo, times(1)).findAll();
        // RedisUtil.set is called asynchronously, so we do not verify its call here
    }

    @Test
    void testGetProjectByName_CacheHit() {
        when(redisUtil.get("project_name_key")).thenReturn(project);

        Project result = projectServiceImpl.getProjectByName("Test Project");

        assertNotNull(result);
        assertEquals("Test Project", result.getName());

        verify(redisUtil, times(1)).get("project_name_key");
        verify(projectRepo, never()).findByName(anyString());
    }

    @Test
    void testGetProjectByName_CacheMiss() {
        when(redisUtil.get("project_name_key")).thenReturn(null);
        when(projectRepo.findByName("Test Project")).thenReturn(Optional.of(project));

        Project result = projectServiceImpl.getProjectByName("Test Project");

        assertNotNull(result);
        assertEquals("Test Project", result.getName());

        verify(redisUtil, times(1)).get("project_name_key");
        verify(projectRepo, times(1)).findByName("Test Project");
        // RedisUtil.set is called asynchronously
    }

    @Test
    void testGetProjectByNameNotFound() {
        when(redisUtil.get("project_name_key")).thenReturn(null);
        when(projectRepo.findByName("Invalid Project")).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.getProjectByName("Invalid Project"));

        verify(redisUtil, times(1)).get("project_name_key");
        verify(projectRepo, times(2)).findByName("Invalid Project");
    }

    @Test
    void testDeleteProjectById() {
        when(projectRepo.existsById(1L)).thenReturn(true);

        String result = projectServiceImpl.deleteProjectById(1L);

        assertEquals("Project with id '1' deleted successfully", result);

        verify(projectRepo, times(1)).existsById(1L);
        verify(projectRepo, times(1)).deleteById(1L);
        verify(redisUtil, times(1)).delete("all_projects");
    }

    @Test
    void testDeleteProjectByIdNotFound() {
        when(projectRepo.existsById(999L)).thenReturn(false);

        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.deleteProjectById(999L));

        verify(projectRepo, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateProject() {
        Long id = 1L;
        Project existing = new Project(id, "Old", Project.Type.COMPANY_MANAGED, "Old", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Project updated = new Project(id, "New", Project.Type.TEAM_MANAGED, "New", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(projectRepo.findById(id)).thenReturn(Optional.of(existing));
        when(projectRepo.save(any(Project.class))).thenReturn(updated);

        Project result = projectServiceImpl.updateProject(id, updated);

        assertNotNull(result);
        assertEquals("New", result.getName());

        verify(projectRepo, times(1)).findById(id);
        verify(projectRepo, times(1)).save(any(Project.class));
        verify(redisUtil, times(1)).delete("all_projects");
    }

    @Test
    void testUpdateProjectNotFound() {
        Long id = 999L;
        Project proj = new Project(id, "Name", Project.Type.TEAM_MANAGED, "Manager", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(projectRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectServiceImpl.updateProject(id, proj));

        verify(projectRepo, never()).save(any(Project.class));
    }

    @Test
    void testGetProjectByManager() {
        List<String> names = List.of("P1", "P2");

        when(projectRepo.findProjectByManager("Manager")).thenReturn(names);

        List<String> result = projectServiceImpl.getProjectByManager("Manager");

        assertNotNull(result);
        assertIterableEquals(names, result);
    }

    @Test
    void testGetProjectByType() {
        List<String> names = List.of("P1", "P2");

        when(projectRepo.findProjectByType(Project.Type.TEAM_MANAGED)).thenReturn(names);

        List<String> result = projectServiceImpl.getProjectByType(Project.Type.TEAM_MANAGED);

        assertNotNull(result);
        assertIterableEquals(names, result);
    }
}
