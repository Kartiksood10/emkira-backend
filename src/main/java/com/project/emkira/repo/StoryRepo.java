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

    @Query("SELECT SUM(s.story_points) FROM Story s WHERE s.sprint.id = :sprintId")
    Long findTotalStoryPointsBySprintId(Long sprintId);

    @Query("SELECT s FROM Story s WHERE s.assignee = :assignee AND s.priority = 'HIGH' ")
    List<Story> findHighPriorityStoriesByAssignee(String assignee);

    @Query("SELECT s.sprint.id, AVG(s.story_points) FROM Story s GROUP BY s.sprint.id")
    // We use List<Object[]> when we are selecting multiple rows in the query (not using DTO)
    // Output : [[1, 5.0], [2, 6.5]] -> sprint 1 average 5.0 and sprint 2 average 6.5
    List<Object[]> averageStoryPointsBySprintId();
}
