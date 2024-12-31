package com.example.my_gradle_spring_app.model;

import java.util.List;

public class ProblemDTO {
    private Integer contestId;
    private String contestName;
    private String userId;
    private String problemName;
    private String problemDescription;
    private String problemInputDescription;
    private String problemOutputDescription;
    private String problemExampleInput;
    private String problemExampleOutput;
    private List<ExampleDTO> examples;

    // Getters and Setters

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

    public List<ExampleDTO> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleDTO> examples) {
        this.examples = examples;
    }
}
