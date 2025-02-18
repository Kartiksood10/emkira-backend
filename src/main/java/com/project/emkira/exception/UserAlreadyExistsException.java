package com.project.emkira.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {

        super(message);
    }
}
