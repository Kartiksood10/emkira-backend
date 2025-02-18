package com.project.emkira.service;

import java.util.List;

public interface ProjectUserService {

    String enrollUserInProject(Long projectId, Long userId, String roleString);

    List<Long> getUserIdsByProjectId(Long projectId);

    List<String> getAccountNamesByProjectId(Long projectId);

    Long getUserCountByProjectId(Long projectId);
}
