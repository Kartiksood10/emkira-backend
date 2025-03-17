package com.project.emkira.service;

import com.project.emkira.dto.LoginRequest;
import com.project.emkira.dto.LoginResponse;
import com.project.emkira.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    LoginResponse loginUser(LoginRequest loginRequest);

    Optional<String> getAccountNameById(Long id);

    Optional<String> getEmailByAccountName(String accountName);
}
