package com.project.emkira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {

        NEW, TO_DO, IN_PROGRESS, DONE
    }

    private String comment;

    private String assignee;

    private String reporter;

    private int story_points;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public enum Priority {

        LOW, MEDIUM, HIGH
    }

    private int estimate;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = true) // Task may belong to a story
    private Story story;

    @ManyToOne
    @JoinColumn(name = "epic_id", nullable = true) // Task MAY belong to an Epic
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false) // Task must belong to a sprint
    private Sprint sprint;

    // One task contains multiple subtasks
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SubTask> subtasks = new ArrayList<>();

}
