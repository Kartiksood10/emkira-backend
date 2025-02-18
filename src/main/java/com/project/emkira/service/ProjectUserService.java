package com.project.emkira.service;

import com.project.emkira.model.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectUserService {

    String enrollUserInProject(Long projectId, Long userId, String roleString);

    List<Long> getUserIdsByProjectId(Long projectId);

    List<String> getAccountNamesByProjectId(Long projectId);
}
