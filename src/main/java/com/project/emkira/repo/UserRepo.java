package com.project.emkira.repo;

import com.project.emkira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Optional as the email may not be present in DB
    // Used when value might be null
    Optional<User> findByEmail(String email);
}
