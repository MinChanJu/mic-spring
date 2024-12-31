package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.Problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByContestId(Integer contestId);
}
