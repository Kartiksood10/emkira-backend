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
@Table(name = "bugs")
public class Bug {

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
    @JoinColumn(name = "story_id", nullable = true) // Bug MAY belong to a Story
    private Story story;

    @ManyToOne
    @JoinColumn(name = "epic_id", nullable = true) // Bug MAY belong to an Epic
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false) // Bug MUST belong to a Sprint
    private Sprint sprint;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BugSubTask> bugSubTasks = new ArrayList<>();

}
