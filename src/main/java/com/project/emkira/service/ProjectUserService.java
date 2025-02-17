package com.project.emkira.service;

public interface ProjectUserService {

    String enrollUserInProject(Long projectId, Long userId, String roleString);
}
