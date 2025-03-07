package com.project.emkira.service;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.model.Epic;

public interface EpicService {

    Epic addEpic(EpicRequest epicRequest);
}
