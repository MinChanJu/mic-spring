package com.example.my_gradle_spring_app.model;

public class Submit {
    private Long problemId;
    private Long score;

    public Submit(Long problemId, Long score) {
        this.problemId = problemId;
        this.score = score;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
