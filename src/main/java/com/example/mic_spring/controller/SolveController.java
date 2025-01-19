package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.entity.Solve;
import com.example.mic_spring.service.SolveService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/solves")
public class SolveController {
    
    @Autowired private SolveService solveService;
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<Solve>>> getAllSolvesByUserId(@PathVariable("userId") String userId) {
        List<Solve> solves = solveService.getAllSolvesByUserId(userId);
        ApiResponse<List<Solve>> response = new ApiResponse<>(200, true, "해결 성공", solves);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<ApiResponse<List<Solve>>> getAllSolvesByProblemId(@PathVariable("problemId") Long problemId) {
        List<Solve> solves = solveService.getAllSolvesByProblemId(problemId);
        ApiResponse<List<Solve>> response = new ApiResponse<>(200, true, "해결 성공", solves);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Solve>> solveProblem(@RequestBody Solve solveDetail) {
        Solve solve = solveService.solveProblem(solveDetail);
        ApiResponse<Solve> response = new ApiResponse<>(200, true, "해결 성공", solve);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
