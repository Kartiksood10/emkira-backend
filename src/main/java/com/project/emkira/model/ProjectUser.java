package com.project.emkira.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_user")
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference // Allows serialization, we want user details in ProjectUser
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference // Prevents recursion in project, Do not need Project details in ProjectUser
    private Project project;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {

        DEVELOPER, TESTER, BUSINESS_ANALYST, ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
