package com.example.mic_spring.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.ContestsAndProblemsDTO;
import com.example.mic_spring.domain.entity.Contest;
import com.example.mic_spring.domain.entity.Problem;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DataServiceTest {
    
    @Autowired
    private DataService dataService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void resetAutoIncrement() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT setval('contests_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM contests), false)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void read() {
        ContestsAndProblemsDTO contestsAndProblems = dataService.getAllContestsAndProblems();
        List<Contest> contests = contestService.getAllContests();
        List<Problem> problems = problemService.getAllProblems();

        assertThat(contestsAndProblems.getContests().size()).isEqualTo(contests.size());
        assertThat(contestsAndProblems.getProblems().size()).isEqualTo(problems.size());
    }
}
