package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "examples", schema = "public" )
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "example_input", nullable = false)
    private String ExampleInput;

    @Column(name = "example_output", nullable = false)
    private String ExampleOutput;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
