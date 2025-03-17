package com.project.emkira.controller;

import com.project.emkira.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public Optional<String> getAccountNameByUserId(@PathVariable Long userId) {

        return userService.getAccountNameById(userId);
    }

    @GetMapping("/username")
    // query params - give key value pair for accountName in postman and get email
    public Optional<String> getEmailByAccountName(@RequestParam String accountName) {

        return userService.getEmailByAccountName(accountName);
    }

}
