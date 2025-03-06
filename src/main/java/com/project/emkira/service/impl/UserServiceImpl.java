package com.project.emkira.service.impl;

import com.project.emkira.dto.LoginRequest;
import com.project.emkira.dto.LoginResponse;
import com.project.emkira.exception.UserAlreadyExistsException;
import com.project.emkira.exception.UserNotFoundException;
import com.project.emkira.model.User;
import com.project.emkira.repo.UserRepo;
import com.project.emkira.service.JwtService;
import com.project.emkira.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public User registerUser(User user) {

        if(userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email ID already exists!");
        }

        if(userRepo.findByAccountName(user.getAccountName()).isPresent()) {
            throw new UserAlreadyExistsException("User with this account name already exists!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepo.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Fetch authenticated user from DB
        User authenticatedUser = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT Token
        String jwtToken = jwtService.generateToken(authenticatedUser);

        // Return JSON response with token
        return new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
    }

    @Override
    public Optional<String> getAccountNameById(Long id) {

        if(userRepo.findById(id).isPresent()) {

            return userRepo.findAccountNameById(id);
        }

        throw new UserNotFoundException("User not found");
    }
}


//    @Override
//    public String loginUser(LoginRequest loginRequest) {
//
//        // Access User data from DB using userRepo (gives stored user data)
//        // Optional as user value may be null and not in DB
//        Optional<User> user = userRepo.findByEmail(loginRequest.getEmail());
//
//        // Checks is user is present and if raw password input == encoded password stored in DB
//        // user.get() -> gets User object, .getPassword() extracts password from it
//        if(user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
//            return "Logged in successfully : " + loginRequest.getEmail();
//        }
//
//        else {
//            return "Wrong email or password";
//        }
//    }

