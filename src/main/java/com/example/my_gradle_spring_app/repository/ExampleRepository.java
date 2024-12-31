package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    List<Example> findByProblemId(Integer problemId);
}
