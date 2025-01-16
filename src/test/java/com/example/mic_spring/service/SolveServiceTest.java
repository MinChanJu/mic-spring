package com.example.mic_spring.service;

import java.sql.Connection;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.entity.Solve;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class SolveServiceTest {

    @Autowired
    private SolveService solveService;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void resetAutoIncrement() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT setval('solves_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM solves), false)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() {
        Solve solve1 = new Solve(null, "test1", 1L, (short) 200, ZonedDateTime.now());
        Solve solve2 = new Solve(null, "test1", 1L, (short) 100, ZonedDateTime.now());
        Solve solve3 = new Solve(null, "test1", 1L, (short) 800, ZonedDateTime.now());

        Solve find1 = solveService.solveProblem(solve1);
        Solve find2 = solveService.solveProblem(solve2);
        Solve find3 = solveService.solveProblem(solve3);

        assertThat(find1.getScore()).isEqualTo(solve1.getScore());
        assertThat(find2.getScore()).isEqualTo(solve1.getScore());
        assertThat(find3.getScore()).isEqualTo(solve3.getScore());
    }

    @Test
    void readAndUpdate() {
        int size1 = solveService.getAllSolves().size();
        int size2 = solveService.getAllSolvesByUserId("test1").size();
        int size3 = solveService.getAllSolvesByProblemId(2L).size();

        Solve solve1 = new Solve(null, "test1", 1L, (short) 200, ZonedDateTime.now());
        Solve solve2 = new Solve(null, "test1", 2L, (short) 340, ZonedDateTime.now());
        Solve solve3 = new Solve(null, "test2", 2L, (short) 700, ZonedDateTime.now());
        Solve solve4 = new Solve(null, "test2", 1L, (short) 100, ZonedDateTime.now());
        Solve solve5 = new Solve(null, "test1", 1L, (short) 800, ZonedDateTime.now());

        solveService.solveProblem(solve1);
        solveService.solveProblem(solve2);
        solveService.solveProblem(solve3);
        solveService.solveProblem(solve4);
        solveService.solveProblem(solve5);

        Solve find1 = solveService.getSolveByUserIdAndProblemId("test1", 1L);
        List<Solve> find2 = solveService.getAllSolves();
        List<Solve> find3 = solveService.getAllSolvesByUserId("test1");
        List<Solve> find4 = solveService.getAllSolvesByProblemId(2L);

        assertThat(find1.getScore()).isEqualTo((short) 800);
        assertThat(find2.size() - size1).isEqualTo(4);
        assertThat(find3.size() - size2).isEqualTo(2);
        assertThat(find4.size() - size3).isEqualTo(2);

        assertThatThrownBy(() -> solveService.getSolveByUserIdAndProblemId("null", null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SOLVE_NOT_FOUND.getMessage());
    }

    @Test
    void delete() {
        Solve solve1 = new Solve(null, "test1", 1L, (short) 200, ZonedDateTime.now());
        Solve solve2 = new Solve(null, "test1", 2L, (short) 340, ZonedDateTime.now());
        Solve solve3 = new Solve(null, "test2", 2L, (short) 700, ZonedDateTime.now());
        Solve solve4 = new Solve(null, "test2", 1L, (short) 100, ZonedDateTime.now());
        Solve solve5 = new Solve(null, "test1", 1L, (short) 800, ZonedDateTime.now());

        solveService.solveProblem(solve1);
        solveService.solveProblem(solve2);
        solveService.solveProblem(solve3);
        solveService.solveProblem(solve4);
        solveService.solveProblem(solve5);

        solveService.deleteSolve(solve1.getId());
        assertThatThrownBy(() -> solveService.deleteSolve(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SOLVE_NOT_FOUND.getMessage());
        solveService.deleteAllSolvesByProblemId(1L);
        assertThatThrownBy(() -> solveService.deleteAllSolvesByProblemId(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.SOLVE_NOT_FOUND.getMessage());
        solveService.deleteAllSolvesByUserId("test2");
    }
}
