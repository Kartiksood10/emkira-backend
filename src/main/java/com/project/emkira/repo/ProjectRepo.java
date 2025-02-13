package com.project.emkira.repo;

import com.project.emkira.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    // Optional as project may not exist
    Optional<Project> findByName(String projectName);
}
