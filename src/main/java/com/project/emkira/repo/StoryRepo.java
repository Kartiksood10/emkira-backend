package com.project.emkira.repo;

import com.project.emkira.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepo extends JpaRepository<Story, Long> {
}
