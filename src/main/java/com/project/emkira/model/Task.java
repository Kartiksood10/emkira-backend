package com.project.emkira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
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
    private Epic.Status status;

    public enum Status {

        NEW, TO_DO, IN_PROGRESS, DONE
    }

    private String comment;

    private String assignee;

    private String reporter;

    private int story_points;

    @Enumerated(EnumType.STRING)
    private Epic.Priority priority;

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
    private List<SubTask> subtasks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Epic.Status getStatus() {
        return status;
    }

    public void setStatus(Epic.Status status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public int getStory_points() {
        return story_points;
    }

    public void setStory_points(int story_points) {
        this.story_points = story_points;
    }

    public Epic.Priority getPriority() {
        return priority;
    }

    public void setPriority(Epic.Priority priority) {
        this.priority = priority;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
