package com.example.mic_spring.domain.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}