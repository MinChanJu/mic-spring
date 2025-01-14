package com.example.my_gradle_spring_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.my_gradle_spring_app.dto.ApiResponse;
import com.example.my_gradle_spring_app.dto.ContestScoreDTO;
import com.example.my_gradle_spring_app.dto.ContestsAndProblemsDTO;
import com.example.my_gradle_spring_app.service.DataService;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class MyController {

    @Autowired private DataService dataService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContestsAndProblemsDTO>> getProblemsAndContests() {
        ContestsAndProblemsDTO contestsAndProblems = dataService.getAllContestsAndProblems();
        ApiResponse<ContestsAndProblemsDTO> response = new ApiResponse<>(200, true, "모든 대회 및 문제 조회 성공", contestsAndProblems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{contestId}")
    public ResponseEntity<ApiResponse<List<ContestScoreDTO>>> getContestScore(@PathVariable Long contestId) {
        List<ContestScoreDTO> contestScores = dataService.getScoreBoardByContestId(contestId);
        ApiResponse<List<ContestScoreDTO>> response = new ApiResponse<>(200, true, "대회 아이디로 스코어보드 조회 성공", contestScores);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
