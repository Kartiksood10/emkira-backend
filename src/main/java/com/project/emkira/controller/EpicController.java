package com.project.emkira.controller;

import com.project.emkira.dto.EpicRequest;
import com.project.emkira.model.Epic;
import com.project.emkira.service.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/epics")
public class EpicController {

    private final EpicService epicService;

    @Autowired
    public EpicController(EpicService epicService) {

        this.epicService = epicService;
    }

    @PostMapping("/add")
    public Epic addEpic(@RequestBody EpicRequest epicRequest) {

        return epicService.addEpic(epicRequest);
    }
}
