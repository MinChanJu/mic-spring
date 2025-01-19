package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.Contest;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByUserId(String userId);
    boolean existsByContestName(String contestName);
}
