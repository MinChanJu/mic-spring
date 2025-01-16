package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.Contest;

import java.util.List;
import java.time.ZonedDateTime;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByEventTimeBefore(ZonedDateTime currentTime);
    List<Contest> findByEventTimeAfter(ZonedDateTime currentTime);
    boolean existsByContestName(String contestName);
}
