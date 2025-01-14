package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "solves", uniqueConstraints = {
        @UniqueConstraint(name = "uk_solves_multi_user_id_problem_id", columnNames = { "user_id", "problem_id" })
})
public class Solve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "score", nullable = false)
    private Short score;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
