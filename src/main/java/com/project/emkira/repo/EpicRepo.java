package com.project.emkira.repo;

import com.project.emkira.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpicRepo extends JpaRepository<Epic, Long> {

    // SELECT * FROM Epic WHERE id = projectId
    List<Epic> findByProjectId(Long projectId);

    // SELECT * FROM Epic WHERE priority = "" Epic.Priority as enum type
    List<Epic> findEpicByPriority(Epic.Priority priority);

    List<Epic> findEpicByStatus(Epic.Status status);
}
