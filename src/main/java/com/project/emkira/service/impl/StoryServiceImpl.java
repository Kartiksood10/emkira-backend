package com.project.emkira.service.impl;

import com.project.emkira.dto.StoryRequest;
import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.exception.UserNotFoundException;
import com.project.emkira.model.Epic;
import com.project.emkira.model.Sprint;
import com.project.emkira.model.Story;
import com.project.emkira.repo.*;
import com.project.emkira.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepo storyRepo;
    private final EpicRepo epicRepo;
    private final SprintRepo sprintRepo;
    private final UserRepo userRepo;
    private final ProjectUserRepo projectUserRepo;
    private final ProjectRepo projectRepo;

    Logger logger = LoggerFactory.getLogger(StoryServiceImpl.class);

    @Autowired
    public StoryServiceImpl(StoryRepo storyRepo, EpicRepo epicRepo, SprintRepo sprintRepo, UserRepo userRepo, ProjectUserRepo projectUserRepo, ProjectRepo projectRepo) {
        this.storyRepo = storyRepo;
        this.epicRepo = epicRepo;
        this.sprintRepo = sprintRepo;
        this.userRepo = userRepo;
        this.projectUserRepo = projectUserRepo;
        this.projectRepo = projectRepo;
    }

    @Override
    public Story addStory(StoryRequest request) {
        try{

            if (request.getProjectId() == null) {
                throw new IllegalArgumentException("Project ID is required");
            }

            Long projectId = request.getProjectId();
            Long assigneeId = request.getAssigneeId();
            Long reporterId = request.getReporterId();

            if (projectRepo.findById(projectId).isEmpty()) {
                throw new ProjectNotFoundException("Project not found");
            }

            if (projectUserRepo.findByUserIdAndProjectId(assigneeId, projectId).isEmpty()) {
                throw new UserNotFoundException("Assignee not found in the project");
            }

            if (projectUserRepo.findByUserIdAndProjectId(reporterId, projectId).isEmpty()) {
                throw new UserNotFoundException("Reporter not found in the project");
            }

            // Comparing Optional<String> and String -> else for Optional returns ""
            if (!userRepo.findAccountNameById(assigneeId).orElse("").equals(request.getAssignee())) {
                throw new UserNotFoundException("Assignee with name not found");
            }

            if (!userRepo.findAccountNameById(reporterId).orElse("").equals(request.getReporter())) {
                throw new UserNotFoundException("Reporter with name not found");
            }

            Sprint sprint = sprintRepo.findById(request.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found"));

            Epic epic = epicRepo.findById(request.getEpicId())
                    .orElseThrow(() -> new IllegalArgumentException("Epic not found"));

            // Since StoryRequest has a projectId in it to denote which project it is
            // we have to ensure the epic inside which it is being added is part of the same project as story
            // similarly the sprint inside which story is being created should be in the same project

            // Project id of story
            Long requestProjectId = request.getProjectId();

            // Project id of the epic in which story is created
            Long epicProjectId = epic.getProject().getId();

            // Project id of the sprint in which the story is created
            Long sprintProjectId = sprint.getProject().getId();

            // Story's projectId should match with its corresponding Epic's projectId
            if (!requestProjectId.equals(epicProjectId)) {
                throw new ProjectNotFoundException("Project IDs don't match for Story and Epic");
            }

            // Story's projectId should match with its corresponding Sprint's projectId
            if (!requestProjectId.equals(sprintProjectId)) {
                throw new ProjectNotFoundException("Project IDs don't match for Story and Sprint");
            }

            Story story = new Story();
            story.setTitle(request.getTitle());
            story.setDescription(request.getDescription());
            story.setStatus(Story.Status.valueOf(request.getStatus()));
            story.setComment(request.getComment());
            story.setAssignee(request.getAssignee());
            story.setReporter(request.getReporter());
            story.setStory_points(request.getStoryPoints());
            story.setPriority(Story.Priority.valueOf(request.getPriority()));
            story.setEstimate(request.getEstimate());

            story.setSprint(sprint);
            story.setEpic(epic);

            return storyRepo.save(story);


        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Story> getStoryByEpicId(Long epicId) {

        if(epicRepo.findById(epicId).isEmpty()) {

            throw new ProjectNotFoundException("Epic not found");
        }

        return storyRepo.findByEpicId(epicId);
    }

    @Override
    public List<Story> getAllStories() {
        return storyRepo.findAll();
    }

    @Override
    public List<Story> getStoryBySprintId(Long sprintId) {

        if(sprintRepo.findById(sprintId).isEmpty()) {

            throw new ProjectNotFoundException("Sprint not found");
        }

        return storyRepo.findBySprintId(sprintId);
    }
}
