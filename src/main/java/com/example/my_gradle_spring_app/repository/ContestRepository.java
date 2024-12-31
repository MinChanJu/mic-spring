package com.example.my_gradle_spring_app.repository;

import com.example.my_gradle_spring_app.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
}
