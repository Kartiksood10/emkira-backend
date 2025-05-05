package com.project.emkira.repo;

import com.project.emkira.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepo extends JpaRepository<Sprint, Long> {

    @Query("SELECT s.name FROM Sprint s WHERE s.project.id = :projectId")
    List<String> findSprintNamesByProjectId(Long projectId);

    // TO_CHAR converts startDate to string
    // FMMonth extracts month name from startDate -> February, March format etc.
    @Query("SELECT s FROM Sprint s WHERE FUNCTION('TO_CHAR', s.startDate, 'FMMonth') = :month")
    List<Sprint> findSprintsByMonth(String month);
}
