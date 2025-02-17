package com.project.emkira.repo;

import com.project.emkira.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectUserRepo extends JpaRepository<ProjectUser, Long> {

    // returns project user for combination of userId and projectId from project_user table
    // Equivalent SQL - Select * from project_user where user_id = userId AND project_id = projectId
    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);

}
