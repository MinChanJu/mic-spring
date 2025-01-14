package com.example.my_gradle_spring_app.model;

public class Submit {
    private Long problemId;
    private Short score;

    public Submit(Long problemId, Short score) {
        this.problemId = problemId;
        this.score = score;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }
}
