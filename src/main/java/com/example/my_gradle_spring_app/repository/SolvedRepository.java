package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.Solved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolvedRepository extends JpaRepository<Solved, Long> {
    List<Solved> findByUserId(String userId);
    List<Solved> findByProblemId(Long problemId);
    Optional<Solved> findByUserIdAndProblemId(String userId, Long problemId);
}
