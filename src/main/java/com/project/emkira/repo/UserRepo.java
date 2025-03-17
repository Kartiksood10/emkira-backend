package com.project.emkira.repo;

import com.project.emkira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Optional as the email may not be present in DB
    // Optional is used when At most one value is returned and could be null
    // Not used in List such as Optional<List<>> as List can be returned empty
    Optional<User> findByEmail(String email);

    Optional<User> findByAccountName(String accountName);

    @Query("SELECT u.accountName FROM User u WHERE u.id = :userId")
    Optional<String> findAccountNameById(Long userId);

    @Query("SELECT u.email FROM User u WHERE u.accountName = :accountName")
    Optional<String> findEmailByAccountName(String accountName);
}
