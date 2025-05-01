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
        redisUtil.delete(PROJECTS_CACHE_KEY);
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
    // @Cacheable(value = "Project Name", key = "#projectName")
    @Override
    public Project getProjectByName(String projectName) {

        Project cachedProject = redisUtil.get("project_name_key");
        try{
            if(cachedProject != null) {
                logger.info("Cache hit! Returning project from Cache");
                return cachedProject;
            }

            logger.info("Cache miss! Fetching data from Database");
            Project projectFromDb = projectRepo.findByName(projectName)
                    .orElseThrow(() -> new ProjectNotFoundException(projectName));

            // Separate thread runs this asynchronously
            CompletableFuture.runAsync(() -> {
                try{
                    redisUtil.set("project_name_key", projectFromDb, Duration.ofSeconds(200));
                    logger.info("Async cache updated successfully");
                } catch(RedisCommandTimeoutException | RedisConnectionFailureException | RedisSystemException ex) {
                    logger.error("Redis is unavailable. Proceeding with DB fetch. Error: {}", ex.getMessage());
                }
                catch (Exception e) {
                    logger.error("Unexpected error during async cache fetch: {}", e.getMessage());
                }
            });
            logger.info("Returning project immediately from DB");
            // After line 103, a separate thread directly returns data from DB
            return projectFromDb;

        } catch (Exception e) {
            logger.error("Error occurred while accessing Redis cache: {}", e.getMessage());
        }

        logger.info("Returning project from DB due to Redis failure");
        return projectRepo.findByName(projectName).orElseThrow(() -> new ProjectNotFoundException(projectName));
    }

    @Override
    public String deleteProjectById(Long id) {

        if(projectRepo.existsById(id)) {

            projectRepo.deleteById(id);
            redisUtil.delete(PROJECTS_CACHE_KEY);
            return "Project with id '" + id + "' deleted successfully";
        }

        throw new ProjectNotFoundException("Project with id '" + id + "' not found");
    }

    // @CachePut(value = "projects", key = "#id")
    // @CacheEvict(value = "projectsCache", key = "'all_projects'")
    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project existingProject = projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id '" + id + "' not found"));

        existingProject.setManager(projectDetails.getManager());
        existingProject.setName(projectDetails.getName());
        existingProject.setType(projectDetails.getType());

        redisUtil.delete(PROJECTS_CACHE_KEY);
        logger.info("Deleted Redis cache for key: {}", PROJECTS_CACHE_KEY);

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
