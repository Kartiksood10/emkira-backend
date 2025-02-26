package com.project.emkira.repo;

import com.project.emkira.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    // Optional as project may not exist
    Optional<Project> findByName(String projectName);

    @Query("SELECT p.name FROM Project p WHERE p.manager = :manager")
    List<String> findProjectByManager(String manager);

    @Query("SELECT p.name FROM Project p WHERE p.type = :type")
    // type is enum so Project.Type
    List<String> findProjectByType(Project.Type type);
}
