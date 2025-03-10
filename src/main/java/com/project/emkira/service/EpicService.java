package com.project.emkira.service;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.model.Epic;

import java.util.List;

public interface EpicService {

    Epic addEpic(EpicRequest epicRequest);

    List<Epic> getProjectById(Long projectId);
}
