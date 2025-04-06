package com.project.emkira.repo;

import com.project.emkira.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StoryRepo extends JpaRepository<Story, Long> {

    @Query("SELECT s FROM Story s WHERE s.epic.id = :epicId")
    List<Story> findByEpicId(Long epicId);

    @Query("SELECT s FROM Story s WHERE s.sprint.id = :sprintId")
    List<Story> findBySprintId(Long sprintId);

    @Modifying // For update/delete in DB
    @Transactional // For change in DB to persist
    @Query("UPDATE Story s SET s.status = :status WHERE s.id = :id")
    // int as it returns no.of rows affected
    int updateStatus(Story.Status status, Long id);
}
