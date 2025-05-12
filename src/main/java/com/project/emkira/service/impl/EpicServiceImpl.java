package com.project.emkira.service.impl;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.dto.EpicStatusRequest;
import com.project.emkira.exception.EpicNotFoundException;
import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.exception.UserNotFoundException;
import com.project.emkira.mapper.EpicMapper;
import com.project.emkira.model.Epic;
import com.project.emkira.model.Project;
import com.project.emkira.repo.EpicRepo;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.repo.ProjectUserRepo;
import com.project.emkira.repo.UserRepo;
import com.project.emkira.service.EpicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpicServiceImpl implements EpicService {

    private final EpicRepo epicRepo;
    private final UserRepo userRepo;
    private final ProjectUserRepo projectUserRepo;
    private final ProjectRepo projectRepo;
    private final EpicMapper epicMapper;

    Logger logger = LoggerFactory.getLogger(EpicServiceImpl.class);

    @Autowired
    public EpicServiceImpl(EpicRepo epicRepo, UserRepo userRepo, ProjectUserRepo projectUserRepo, ProjectRepo projectRepo, EpicMapper epicMapper) {
        this.epicRepo = epicRepo;
        this.userRepo = userRepo;
        this.projectUserRepo = projectUserRepo;
        this.projectRepo = projectRepo;
        this.epicMapper = epicMapper;
    }

    @Override
    public Epic addEpic(EpicRequest request) {
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

            // Convert DTO to Entity
            // Epic Request is just for API request since we want assignee_id and reporter_id
            // Converting back to Entity since in db we store entity values
//            Epic epic = new Epic();
//            epic.setTitle(request.getTitle());
//            epic.setDescription(request.getDescription());
//            epic.setStatus(Epic.Status.valueOf(request.getStatus())); // Convert String to Enum
//            epic.setComment(request.getComment());
//            epic.setAssignee(request.getAssignee());
//            epic.setReporter(request.getReporter());
//            epic.setStory_points(request.getStoryPoints());
//            epic.setPriority(Epic.Priority.valueOf(request.getPriority())); // Convert String to Enum
//            epic.setEstimate(request.getEstimate());

            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));

            // Using mapper to convert dto back to entity
            Epic epic = epicMapper.toEntity(request, project);

            return epicRepo.save(epic);

        } catch(Exception e){
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Epic> getProjectById(Long projectId) {

        return epicRepo.findByProjectId(projectId);
    }

    @Override
    public Epic updateEpicStatus(Long epicId, EpicStatusRequest request) {

        try{
            Epic epic = epicRepo.findById(epicId).
                    orElseThrow(() -> new EpicNotFoundException("Epic not found"));

            epic.setStatus(request.getStatus());

            return epicRepo.save(epic);
        } catch(Exception e){

            logger.error(e.getMessage());
        }

        return null;

    }

    @Override
    public String deleteEpic(Long epicId) {

        epicRepo.findById(epicId).
                orElseThrow(() -> new EpicNotFoundException("Epic not found"));

        epicRepo.deleteById(epicId);
        return "Epic deleted successfully";
    }

    @Override
    public List<Epic> getEpicByPriority(Epic.Priority priority) {

        return epicRepo.findEpicByPriority(priority);
    }

    @Override
    public List<Epic> getEpicByStatus(Epic.Status status) {

        return epicRepo.findEpicByStatus(status);
    }

}
