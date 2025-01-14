package com.example.my_gradle_spring_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.my_gradle_spring_app.dto.ApiResponse;
import com.example.my_gradle_spring_app.model.Solve;
import com.example.my_gradle_spring_app.service.SolveService;

import java.util.List;

@RestController
@RequestMapping("/api/solves")
public class SolveController {
    
    @Autowired private SolveService solveService;

    @PostMapping
    public ResponseEntity<ApiResponse<Solve>> solveProblem(@RequestBody Solve solveDetail) {
        Solve solve = solveService.solveProblem(solveDetail);
        ApiResponse<Solve> response = new ApiResponse<>(200, true, "해결 성공", solve);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Solve>>> getSolveOfUser(@PathVariable String userId) {
        List<Solve> solves = solveService.getAllSolvesByUserId(userId);
        ApiResponse<List<Solve>> response = new ApiResponse<>(200, true, "해결 성공", solves);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/problem/{problemId}")
    public ResponseEntity<ApiResponse<List<Solve>>> getSolveOfUser(@PathVariable Long problemId) {
        List<Solve> solves = solveService.getAllSolvesByProblemId(problemId);
        ApiResponse<List<Solve>> response = new ApiResponse<>(200, true, "해결 성공", solves);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
