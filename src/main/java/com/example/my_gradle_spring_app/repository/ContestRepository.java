package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.Contest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.OffsetDateTime;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByEventTimeBefore(OffsetDateTime currentTime);
    List<Contest> findByEventTimeAfter(OffsetDateTime currentTime);
    boolean existsByContestName(String contestName);
}
