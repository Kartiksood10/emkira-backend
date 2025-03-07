package com.project.emkira.repo;

import com.project.emkira.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpicRepo extends JpaRepository<Epic, Long> {
}
