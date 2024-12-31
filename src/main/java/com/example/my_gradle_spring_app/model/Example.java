package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "examples", schema = "public" )
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "problem_id", nullable = false)
    private Integer problemId;

    @Column(name = "example_input", nullable = false, columnDefinition = "TEXT")
    private String ExampleInput;

    @Column(name = "example_output", nullable = false, columnDefinition = "TEXT")
    private String ExampleOutput;

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

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getExampleInput() {
        return ExampleInput;
    }

    public void setExampleInput(String exampleInput) {
        ExampleInput = exampleInput;
    }

    public String getExampleOutput() {
        return ExampleOutput;
    }

    public void setExampleOutput(String exampleOutput) {
        ExampleOutput = exampleOutput;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
