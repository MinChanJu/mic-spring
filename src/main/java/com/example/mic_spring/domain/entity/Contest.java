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

  @Column(name = "user_id", columnDefinition = "TEXT", nullable = false)
  private String userId;

  @Column(name = "contest_name", columnDefinition = "TEXT", nullable = false)
  private String contestName;

  @Column(name = "contest_description", columnDefinition = "TEXT", nullable = false)
  private String contestDescription;

  @Column(name = "contest_pw", columnDefinition = "TEXT")
  private String contestPw;

  @Column(name = "start_time")
  private ZonedDateTime startTime;

  @Column(name = "end_time")
  private ZonedDateTime endTime;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt = ZonedDateTime.now();
}