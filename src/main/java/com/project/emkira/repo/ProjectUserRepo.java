package com.project.emkira.repo;

import com.project.emkira.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectUserRepo extends JpaRepository<ProjectUser, Long> {

    ProjectUser findByUserId(Long userId);
    ProjectUser findByProjectId(Long projectId);

    // returns project user for combination of userId and projectId from project_user table
    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);

}
