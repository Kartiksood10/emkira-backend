package com.project.emkira.repo;

import com.project.emkira.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepo extends JpaRepository<Sprint, Long> {

    @Query("SELECT s.name FROM Sprint s WHERE s.project.id = :projectId")
    List<String> findSprintNamesByProjectId(Long projectId);
}
