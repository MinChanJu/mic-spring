package com.example.my_gradle_spring_app.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "contests", uniqueConstraints = {
        @UniqueConstraint(name = "uk_contests_contest_name", columnNames = "contest_name")
})
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "contest_name", nullable = false)
    private String contestName;

    @Column(name = "contest_description", nullable = false)
    private String contestDescription;

    @Column(name = "contest_pw", nullable = false)
    private String contestPw;

    @Column(name = "time", nullable = false)
    private Long time;

    @Column(name = "event_time", nullable = false)
    private ZonedDateTime eventTime;

    @Column(name = "created_at", nullable = false)
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

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestDescription() {
        return contestDescription;
    }

    public void setContestDescription(String contestDescription) {
        this.contestDescription = contestDescription;
    }

    public String getContestPw() {
        return contestPw;
    }

    public void setContestPw(String contestPw) {
        this.contestPw = contestPw;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public ZonedDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(ZonedDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
