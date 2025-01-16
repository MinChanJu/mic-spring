package com.example.mic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mic_spring.domain.entity.Example;

import java.util.List;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    List<Example> findByProblemId(Long problemId);
}
