package com.example.my_gradle_spring_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.my_gradle_spring_app.model.Solve;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolveRepository extends JpaRepository<Solve, Long> {
    List<Solve> findByUserId(String userId);
    List<Solve> findByProblemId(Long problemId);
    Optional<Solve> findByUserIdAndProblemId(String userId, Long problemId);
}
