package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.dto.CodeDTO;
import com.example.mic_spring.service.CodeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired private CodeService codeService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> submitCode(@RequestBody CodeDTO codeDTO) {
        String result = codeService.runCode(codeDTO);
        ApiResponse<String> response = new ApiResponse<>(200, true, "코드 테스트 성공", result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
