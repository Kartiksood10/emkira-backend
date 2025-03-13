package com.project.emkira.dto;

import com.project.emkira.model.Epic.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpicStatusRequest {

    private Status status;
}
