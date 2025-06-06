package com.project.emkira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "sprints")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ONGOING, COMPLETED
    }

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // One sprint contains multiple stories, tasks and bugs
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Story> stories = new ArrayList<>();

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>(); // Tasks that are independent of Stories

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Bug> bugs = new ArrayList<>();

}
