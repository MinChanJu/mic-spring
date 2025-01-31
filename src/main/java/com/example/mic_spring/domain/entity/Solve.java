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
@Table(name = "solves", uniqueConstraints = {
    @UniqueConstraint(name = "uk_solves_multi_user_id_problem_id", columnNames = { "user_id", "problem_id" })
})
public class Solve {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", columnDefinition = "TEXT", nullable = false)
  private String userId;

  @Column(name = "problem_id", nullable = false)
  private Long problemId;

  @Column(name = "score", nullable = false)
  private Short score;

  @Column(name = "lang", columnDefinition = "TEXT", nullable = false)
  private String lang;

  @Column(name = "code", columnDefinition = "TEXT", nullable = false)
  private String code;

  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt = ZonedDateTime.now();
}
