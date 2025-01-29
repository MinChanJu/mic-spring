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
@Table(name = "problems", uniqueConstraints = {
        @UniqueConstraint(name = "uk_problems_problem_name", columnNames = "problem_name")
})
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id")
    private Long contestId;

    @Column(name = "user_id", columnDefinition = "TEXT", nullable = false)
    private String userId;

    @Column(name = "problem_name", columnDefinition = "TEXT", nullable = false)
    private String problemName;

    @Column(name = "problem_description", columnDefinition = "TEXT", nullable = false)
    private String problemDescription;

    @Column(name = "problem_input_description", columnDefinition = "TEXT", nullable = false)
    private String problemInputDescription;

    @Column(name = "problem_output_description", columnDefinition = "TEXT", nullable = false)
    private String problemOutputDescription;

    @Column(name = "problem_example_input", columnDefinition = "TEXT", nullable = false)
    private String problemExampleInput;

    @Column(name = "problem_example_output", columnDefinition = "TEXT", nullable = false)
    private String problemExampleOutput;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
