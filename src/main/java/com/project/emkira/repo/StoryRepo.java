package com.project.emkira.repo;

import com.project.emkira.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepo extends JpaRepository<Story, Long> {

    @Query("SELECT s FROM Story s WHERE s.epic.id = :epicId")
    List<Story> findByEpicId(Long epicId);
}
