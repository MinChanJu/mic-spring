package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "problems", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = "problem_name")
})
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id")
    private Integer contestId;

    @Column(name = "contest_name", length = 100)
    private String contestName;

    @Column(name = "user_id", nullable = false, columnDefinition = "TEXT")
    private String userId;

    @Column(name = "problem_name", nullable = false, length = 100, unique = true)
    private String problemName;

    @Column(name = "problem_description", nullable = false, columnDefinition = "TEXT")
    private String problemDescription;

    @Column(name = "problem_input_description", nullable = false, columnDefinition = "TEXT")
    private String problemInputDescription;

    @Column(name = "problem_output_description", nullable = false, columnDefinition = "TEXT")
    private String problemOutputDescription;

    @Column(name = "problem_example_input", nullable = false, columnDefinition = "TEXT")
    private String problemExampleInput;

    @Column(name = "problem_example_output", nullable = false, columnDefinition = "TEXT")
    private String problemExampleOutput;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getProblemInputDescription() {
        return problemInputDescription;
    }

    public void setProblemInputDescription(String problemInputDescription) {
        this.problemInputDescription = problemInputDescription;
    }

    public String getProblemOutputDescription() {
        return problemOutputDescription;
    }

    public void setProblemOutputDescription(String problemOutputDescription) {
        this.problemOutputDescription = problemOutputDescription;
    }

    public String getProblemExampleInput() {
        return problemExampleInput;
    }

    public void setProblemExampleInput(String problemExampleInput) {
        this.problemExampleInput = problemExampleInput;
    }

    public String getProblemExampleOutput() {
        return problemExampleOutput;
    }

    public void setProblemExampleOutput(String problemExampleOutput) {
        this.problemExampleOutput = problemExampleOutput;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
