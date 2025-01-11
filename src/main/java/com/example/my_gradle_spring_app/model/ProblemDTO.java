package com.example.my_gradle_spring_app.model;

import java.util.List;

public class ProblemDTO {
    private Problem problem;
    private List<Example> examples;

    // Getters and Setters

    public Problem getProblem() {
        return problem;
    }
    
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }
}
