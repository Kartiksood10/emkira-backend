package com.project.emkira.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectUserRequest {

    private Long userId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum  Role {

        DEVELOPER, TESTER, BUSINESS_ANALYST, ADMIN
    }
}
