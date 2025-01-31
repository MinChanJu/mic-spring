package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);

  List<User> findByContestId(Long contestId);

  Optional<User> findByUserIdAndUserPw(String userId, String userPw);

  boolean existsByUserId(String userId);
}