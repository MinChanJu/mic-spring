package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    List<User> findByContestId(Long contestId);
    boolean existsByUserId(String userId);
}