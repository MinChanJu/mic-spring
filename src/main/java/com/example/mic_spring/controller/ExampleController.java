package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.entity.Example;
import com.example.mic_spring.service.ExampleService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {

    @Autowired private ExampleService exampleService;

    @GetMapping("/{problemId}")
    public ResponseEntity<ApiResponse<List<Example>>> getAllExamplesByProblemId(@PathVariable("problemId") Long problemId) {
        List<Example> examples = exampleService.getAllExamplesByProblemId(problemId);
        ApiResponse<List<Example>> response = new ApiResponse<>(200, true, "예제 문제번호로 조회 성공", examples);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContestById(@PathVariable("id") Long id) {
        exampleService.deleteExample(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "예제 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
