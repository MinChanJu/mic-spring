package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.Contest;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByUserId(String userId);
    List<Contest> findByUserIdOrderByIdAsc(String userId);
    @Query("SELECT c FROM Contest c ORDER BY " +
           "CASE WHEN c.startTime IS NULL THEN 1 ELSE 0 END, " +
           "c.startTime DESC, " +
           "c.createdAt DESC")
    List<Contest> findAllOrderByStartTimeDescOrCreatedAtDesc();
    boolean existsByContestName(String contestName);
}
