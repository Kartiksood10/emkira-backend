package com.project.emkira.repo;

import com.project.emkira.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepo extends JpaRepository<ProjectUser, Long> {

    // returns project user for combination of userId and projectId from project_user table
    // Equivalent SQL - Select * from project_user where user_id = userId AND project_id = projectId
    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);

    @Query("SELECT pu.user.id FROM ProjectUser pu WHERE pu.project.id = :projectId")
    List<Long> findAllUserIdsByProjectId(Long projectId);

    // User table has accountName attribute, which is present in ProjectUser as User user
    // So user.accountName gives accountName
    // Project has id attribute -> called as project.id from ProjectUser as Project project
    @Query("SELECT pu.user.accountName FROM ProjectUser pu WHERE pu.project.id = :projectId")
    List<String> findAllAccountNamesByProjectId(Long projectId);

    @Query("SELECT COUNT(pu.user.id) FROM ProjectUser pu WHERE pu.project.id = :projectId")
    Long findUserCountByProjectId(Long projectId);

}
