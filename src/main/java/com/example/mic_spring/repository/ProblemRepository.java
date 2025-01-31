package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.Problem;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
  List<Problem> findAllByOrderByIdAsc();

  List<Problem> findByContestIdOrderByIdAsc(Long contestId);

  List<Problem> findByUserId(String userId);

  List<Problem> findByUserIdOrderByIdAsc(String userId);

  boolean existsByProblemName(String problemName);
}
