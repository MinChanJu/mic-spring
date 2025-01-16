package com.example.mic_spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.CodeDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CodeServiceTest {
    
    @Autowired
    private CodeService codeService;

    @Test
    void code() {
        CodeDTO codeDTO = new CodeDTO("print('Hello')", "Python", 2L);

        String result1 = codeService.runCode(codeDTO);

        assertThat(result1).isEqualTo("50.0");
    }
}
