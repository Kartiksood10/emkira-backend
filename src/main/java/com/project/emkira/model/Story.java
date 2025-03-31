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
@Table(name = "stories")
public class Story {

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
    @JoinColumn(name = "epic_id", nullable = false)
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false) // Story belongs to a Sprint
    private Sprint sprint;

    // One story contains multiple tasks
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

}