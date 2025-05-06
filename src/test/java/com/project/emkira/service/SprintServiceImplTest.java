package com.project.emkira.service;

import com.project.emkira.model.Project;
import com.project.emkira.model.Sprint;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.repo.SprintRepo;
import com.project.emkira.service.impl.SprintServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SprintServiceImplTest {

    @Mock
    private SprintRepo sprintRepo;

    @Mock
    private ProjectRepo projectRepo;

    @InjectMocks
    private SprintServiceImpl sprintService;

    private Sprint sprint;
    private Project project;

    @BeforeEach
    void setUp() {
        sprint = new Sprint();
        sprint.setId(1L);
        sprint.setName("Test sprint");
        sprint.setStartDate(LocalDate.of(2025, 1, 1));
        sprint.setEndDate(LocalDate.of(2025, 1, 2));
        sprint.setStatus(Sprint.Status.ONGOING);

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setType(Project.Type.COMPANY_MANAGED);
        project.setManager("Test Manager");

    }

    @Test
    void addSprint(){

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        when(sprintRepo.save(sprint)).thenReturn(sprint);

        Sprint result = sprintService.addSprint(sprint, project.getId());

        assertNotNull(result);
        assertEquals("Test sprint", result.getName());
        assertEquals(LocalDate.of(2025, 1, 1), result.getStartDate());
        assertEquals(LocalDate.of(2025, 1, 2), result.getEndDate());
        assertEquals(Sprint.Status.ONGOING, result.getStatus());
        assertEquals(project.getId(), result.getProject().getId());
        verify(projectRepo, times(1)).findById(project.getId());
        verify(sprintRepo, times(1)).save(any(Sprint.class));
    }

    @Test
    void getSprintByProjectId(){

        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        when(sprintRepo.findSprintNamesByProjectId(project.getId())).thenReturn(List.of("Sprint 1", "Sprint 2"));

        List<String> sprints = sprintService.getSprintListByProjectId(project.getId());

        assertNotNull(sprints);
        assertEquals(2, sprints.size());

        verify(projectRepo, times(1)).findById(project.getId());
        verify(sprintRepo, times(1)).findSprintNamesByProjectId(project.getId());
    }

    @Test
    void getSprintByMonth(){

        Sprint sprint1 = new Sprint();
        sprint1.setStartDate(LocalDate.of(2025, 1, 1));

        Sprint sprint2 = new Sprint();
        sprint2.setStartDate(LocalDate.of(2025, 1, 2));

        List<Sprint> sprintList = List.of(sprint1, sprint2);
        when(sprintRepo.findSprintsByMonth("January")).thenReturn(sprintList);

        List<Sprint> sprints = sprintService.getSprintsByMonth("January");
        assertNotNull(sprints);
        assertEquals(2, sprints.size());

        verify(sprintRepo, times(1)).findSprintsByMonth("January");
    }
}
