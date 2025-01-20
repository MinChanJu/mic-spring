package com.example.mic_spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.CodeDTO;
import com.example.mic_spring.domain.dto.CodeResultDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CodeServiceTest {
    
    @Autowired
    private CodeService codeService;

    @Test
    void code() {
        CodeDTO codeDTO = new CodeDTO("test", "print('Hello')", "Python", 2L);

        CodeResultDTO result1 = codeService.runCode(codeDTO);

        assertThat(result1.getResult()).isEqualTo("50.0");
    }
}
