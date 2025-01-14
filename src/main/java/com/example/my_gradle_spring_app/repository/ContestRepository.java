package com.example.my_gradle_spring_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.my_gradle_spring_app.model.Contest;

import java.util.List;
import java.time.ZonedDateTime;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByEventTimeBefore(ZonedDateTime currentTime);
    List<Contest> findByEventTimeAfter(ZonedDateTime currentTime);
    boolean existsByContestName(String contestName);
}
