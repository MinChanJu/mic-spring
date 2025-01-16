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

import com.example.mic_spring.domain.entity.Example;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ExampleServiceTest {
    
    @Autowired
    private ExampleService exampleService;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void resetAutoIncrement() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT setval('examples_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM examples), false)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() {
        Example example1 = new Example(null, 1L, "in1", "out1", ZonedDateTime.now());

        Example find1 = exampleService.createExample(example1);

        assertThat(find1.getExampleInput()).isEqualTo(example1.getExampleInput());
    }

    @Test
    void read() {
        int size1 = exampleService.getAllExamples().size();
        int size2 = exampleService.getAllExamplesByProblemId(2L).size();

        Example example1 = new Example(null, 1L, "in1", "out1", ZonedDateTime.now());
        Example example2 = new Example(null, 2L, "in2", "out2", ZonedDateTime.now());
        Example example3 = new Example(null, 2L, "in3", "out3", ZonedDateTime.now());
        Example example4 = new Example(null, 1L, "in4", "out4", ZonedDateTime.now());
        Example example5 = new Example(null, 1L, "in5", "out5", ZonedDateTime.now());

        exampleService.createExample(example1);
        exampleService.createExample(example2);
        exampleService.createExample(example3);
        exampleService.createExample(example4);
        exampleService.createExample(example5);

        List<Example> find1 = exampleService.getAllExamples();
        List<Example> find2 = exampleService.getAllExamplesByProblemId(2L);

        assertThat(find1.size() - size1).isEqualTo(5);
        assertThat(find2.size() - size2).isEqualTo(2);
    }

    @Test
    void update() {
        Example example1 = new Example(null, 1L, "in1", "out1", ZonedDateTime.now());
        Example example2 = new Example(null, 2L, "in2", "out2", ZonedDateTime.now());

        exampleService.createExample(example1);
        exampleService.createExample(example2);

        example1.setExampleInput("null");
        example1.setExampleOutput("as");

        Example noExistExample2 = new Example(null, 2L, "out2", "out2", ZonedDateTime.now());

        Example update1 = exampleService.updateExample(example1);

        assertThat(update1.getExampleInput()).isEqualTo(example1.getExampleInput());
        assertThatThrownBy(() -> exampleService.updateExample(noExistExample2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EXAMPLE_NOT_FOUND.getMessage());
    }

    @Test
    void delete() {
        Example example1 = new Example(null, 1L, "in1", "out1", ZonedDateTime.now());
        Example example2 = new Example(null, 2L, "in2", "out2", ZonedDateTime.now());
        Example example3 = new Example(null, 2L, "in3", "out3", ZonedDateTime.now());
        Example example4 = new Example(null, 1L, "in4", "out4", ZonedDateTime.now());
        Example example5 = new Example(null, 2L, "in5", "out5", ZonedDateTime.now());

        exampleService.createExample(example1);
        exampleService.createExample(example2);
        exampleService.createExample(example3);
        exampleService.createExample(example4);
        exampleService.createExample(example5);

        exampleService.deleteExample(example1.getId());
        assertThatThrownBy(() -> exampleService.deleteExample(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EXAMPLE_NOT_FOUND.getMessage());
        exampleService.deleteAllExamplesByProblemId(2L);
    }
}
