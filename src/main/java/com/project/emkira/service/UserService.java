package com.project.emkira.service;

import com.project.emkira.dto.LoginRequest;
import com.project.emkira.dto.LoginResponse;
import com.project.emkira.model.User;

public interface UserService {

    public User registerUser(User user);

    public LoginResponse loginUser(LoginRequest loginRequest);
}
