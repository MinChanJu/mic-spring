package com.example.my_gradle_spring_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.my_gradle_spring_app.dto.ApiResponse;
import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.service.ExampleService;

import java.util.List;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {

    @Autowired private ExampleService exampleService;

    @PostMapping("/{problemId}")
    public ResponseEntity<ApiResponse<List<Example>>> getProblemExamples(@PathVariable Long problemId) {
        List<Example> examples = exampleService.getAllExamplesByProblemId(problemId);
        ApiResponse<List<Example>> response = new ApiResponse<>(200, true, "예제 문제번호로 조회 성공", examples);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExample(@PathVariable Long id) {
        exampleService.deleteExample(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "예제 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
