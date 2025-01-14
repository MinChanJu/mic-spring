package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "problems", uniqueConstraints = {
        @UniqueConstraint(name = "uk_problems_problem_name", columnNames = "problem_name")
})
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "problem_name", nullable = false)
    private String problemName;

    @Column(name = "problem_description", nullable = false)
    private String problemDescription;

    @Column(name = "problem_input_description", nullable = false)
    private String problemInputDescription;

    @Column(name = "problem_output_description", nullable = false)
    private String problemOutputDescription;

    @Column(name = "problem_example_input", nullable = false)
    private String problemExampleInput;

    @Column(name = "problem_example_output", nullable = false)
    private String problemExampleOutput;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
