package com.project.emkira.service.impl;


import com.project.emkira.exception.ProjectNotFoundException;
import com.project.emkira.model.Project;
import com.project.emkira.repo.ProjectRepo;
import com.project.emkira.service.ProjectService;
import com.project.emkira.util.RedisUtil;
import io.lettuce.core.RedisCommandTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;

    private static final String PROJECTS_CACHE_KEY = "all_projects";

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final RedisUtil redisUtil;

    @Autowired
    public ProjectServiceImpl(ProjectRepo projectRepo, RedisUtil redisUtil) {
        this.projectRepo = projectRepo;
        this.redisUtil = redisUtil;
    }

    @Override
    public Project addProject(Project project) {
        return projectRepo.save(project);
    }

    // @Cacheable(value = "Projects")
    @Override
    public List<Project> getAllProjects() {
        try {
            // Attempt to fetch data from redis cache
            List<Project> cachedProjects = redisUtil.get(PROJECTS_CACHE_KEY);

            if (cachedProjects != null) {
                logger.info("Cache hit! Returning data from Redis");
                return cachedProjects;
            }

            logger.info("Cache miss! Fetching data from Database...");
            // fetching data from db to add into redis
            List<Project> projectsDB = projectRepo.findAll();

            // Async cache population while data is retrieved by DB
            CompletableFuture.runAsync(() -> {
                try {
                    logger.info("Adding data into Redis");
                    redisUtil.set(PROJECTS_CACHE_KEY, projectsDB, Duration.ofSeconds(200));
                    logger.info("Async cache update successful for key: {}", PROJECTS_CACHE_KEY);
                } catch (RedisConnectionFailureException | RedisSystemException |
                         RedisCommandTimeoutException redisEx) {
                    logger.warn("Redis is unavailable. Proceeding with DB fetch. Error: {}", redisEx.getMessage());
                } catch (Exception ex) {
                    logger.error("Unexpected error during async cache update: {}", ex.getMessage());
                }
            });

            // Returning data from DB immediately while cache is updated asynchronously
            logger.info("Returning data immediately from DB while cache update");
            return projectsDB;

        } catch (RedisConnectionFailureException | RedisSystemException |
                 RedisCommandTimeoutException redisEx) {
            logger.warn("Redis is unavailable. Proceeding with DB: {}", redisEx.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error while accessing Redis cache: {}", ex.getMessage());
        }

        // Fallback: Redis failed, so fetch directly from DB
        logger.info("Returning data from DB due to Redis failure");
        return projectRepo.findAll();
    }


    // key is the parameter present in the function which redis uses to store data
    @Cacheable(value = "Project Name", key = "#projectName")
    @Override
    public Project getProjectByName(String projectName) {
        // This will be logged when the data is fetched from the database
        logger.info("Fetching project with name '{}' from the database", projectName);

        // Simulate DB call and return project
        return projectRepo.findByName(projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Project with name '" + projectName + "' not found"));
    }

    @Override
    public String deleteProjectById(Long id) {

        if(projectRepo.existsById(id)) {

            projectRepo.deleteById(id);
            return "Project with id '" + id + "' deleted successfully";
        }

        throw new ProjectNotFoundException("Project with id '" + id + "' not found");
    }

    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project existingProject = projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id '" + id + "' not found"));

        existingProject.setManager(projectDetails.getManager());
        existingProject.setName(projectDetails.getName());
        existingProject.setType(projectDetails.getType());

        return projectRepo.save(existingProject);
    }

    @Override
    public List<String> getProjectByManager(String manager) {
        return projectRepo.findProjectByManager(manager);
    }

    @Override
    public List<String> getProjectByType(Project.Type type) {
        return projectRepo.findProjectByType(type);
    }


}
