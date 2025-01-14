package com.example.my_gradle_spring_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.my_gradle_spring_app.dto.ApiResponse;
import com.example.my_gradle_spring_app.dto.CodeDTO;
import com.example.my_gradle_spring_app.service.CodeService;

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
