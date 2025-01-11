package com.example.my_gradle_spring_app.model;

public class Submit {
    private Long problemId;
    private String score;

    public Submit(Long problemId, String score) {
        this.problemId = problemId;
        this.score = score;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
