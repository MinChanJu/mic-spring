package com.example.mic_spring.service;

import java.sql.Connection;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.ProblemDTO;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void resetAutoIncrement() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT setval('problems_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM problems), false)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() {
        Problem problem1 = new Problem(null, 1L, "test1", "test11", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now());
        Problem problem2 = new Problem(null, 1L, "test1", "test11", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now());

        Problem find1 = problemService.createProblem(new ProblemDTO(problem1, new ArrayList<>()));

        assertThat(find1.getProblemName()).isEqualTo(problem1.getProblemName());
        assertThatThrownBy(() -> problemService.createProblem(new ProblemDTO(problem2, new ArrayList<>())))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_PROBLEM_NAME.getMessage());
    }

    @Test
    void read() {
        int size1 = problemService.getAllProblems().size();
        int size2 = problemService.getAllProblemsByContestId(2L).size();

        Problem problem1 = new Problem(null, 1L, "test1", "test11", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().plusDays(2));
        Problem problem2 = new Problem(null, 1L, "test1", "test22", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().minusDays(3));
        Problem problem3 = new Problem(null, 2L, "test1", "test33", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().minusHours(5));
        Problem problem4 = new Problem(null, 2L, "test1", "test44", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().plusHours(12));
        Problem problem5 = new Problem(null, 2L, "test1", "test55", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().plusMinutes(120));

        problemService.createProblem(new ProblemDTO(problem1, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem2, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem3, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem4, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem5, new ArrayList<>()));

        Problem find1 = problemService.getProblemById(problem1.getId());
        List<Problem> find2 = problemService.getAllProblems();
        List<Problem> find3 = problemService.getAllProblemsByContestId(2L);

        assertThat(find1.getProblemName()).isEqualTo(problem1.getProblemName());
        assertThat(find2.size() - size1).isEqualTo(5);
        assertThat(find3.size() - size2).isEqualTo(3);
        assertThatThrownBy(() -> problemService.getProblemById(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PROBLEM_NOT_FOUND.getMessage());
    }

    @Test
    void update() {
        Problem problem1 = new Problem(null, 1L, "test1", "test11", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().plusDays(2));
        Problem problem2 = new Problem(null, 1L, "test1", "test22", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().minusDays(3));

        problemService.createProblem(new ProblemDTO(problem1, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem2, new ArrayList<>()));

        problem1.setProblemDescription("null");
        problem1.setProblemExampleInput("12");
        problem1.setProblemInputDescription("sssd");

        Problem noExistProblem2 = new Problem(null, 1L, "test1", "test22", "sdf", "sdf", "gds", "hg", "test11",
                ZonedDateTime.now().minusDays(3));

        Problem update1 = problemService.updateProblem(new ProblemDTO(problem1, new ArrayList<>()));

        assertThat(update1.getProblemDescription()).isEqualTo(problem1.getProblemDescription());
        assertThatThrownBy(() -> problemService.updateProblem(new ProblemDTO(noExistProblem2, new ArrayList<>())))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PROBLEM_NOT_FOUND.getMessage());
    }

    @Test
    void delete() {
        Problem problem1 = new Problem(null, 1L, "test1", "test11", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().plusDays(2));
        Problem problem2 = new Problem(null, 1L, "test1", "test22", "test11", "test11", "test11", "test11", "test11",
                ZonedDateTime.now().minusDays(3));

        problemService.createProblem(new ProblemDTO(problem1, new ArrayList<>()));
        problemService.createProblem(new ProblemDTO(problem2, new ArrayList<>()));

        problemService.deleteProblem(problem1.getId());
        assertThatThrownBy(() -> problemService.deleteProblem(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PROBLEM_NOT_FOUND.getMessage());
    }
}
