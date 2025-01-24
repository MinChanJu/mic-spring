package com.example.mic_spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.CodeResultDTO;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

@SpringBootTest
@Transactional
public class CodeServiceTest {
    
    @Test
    void code() {
        CodeResultDTO result1 = new CodeResultDTO("50.0", new ArrayList<>());

        assertThat(result1.getResult()).isEqualTo("50.0");
    }
}
