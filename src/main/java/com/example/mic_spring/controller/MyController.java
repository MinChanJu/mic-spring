package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.dto.CodeDTO;
import com.example.mic_spring.domain.dto.ContestScoreDTO;
import com.example.mic_spring.domain.dto.ContestsAndProblemsDTO;
import com.example.mic_spring.service.CodeService;
import com.example.mic_spring.service.DataService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class MyController {

    @Autowired private CodeService codeService;
    @Autowired private DataService dataService;

    @GetMapping
    public ResponseEntity<ApiResponse<ContestsAndProblemsDTO>> getAllContestsAndProblems() {
        ContestsAndProblemsDTO contestsAndProblems = dataService.getAllContestsAndProblems();
        ApiResponse<ContestsAndProblemsDTO> response = new ApiResponse<>(200, true, "모든 대회 및 문제 조회 성공", contestsAndProblems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<ContestsAndProblemsDTO>> getAllFilterContestsAndProblems() {
        ContestsAndProblemsDTO contestsAndProblems = dataService.getAllFilterContestsAndProblems();
        ApiResponse<ContestsAndProblemsDTO> response = new ApiResponse<>(200, true, "대회 및 문제 조회 성공", contestsAndProblems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<List<ContestScoreDTO>>> getScoreBoardByContestId(@PathVariable("contestId") Long contestId) {
        List<ContestScoreDTO> contestScores = dataService.getScoreBoardByContestId(contestId);
        ApiResponse<List<ContestScoreDTO>> response = new ApiResponse<>(200, true, "대회 아이디로 스코어보드 조회 성공", contestScores);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/code")
    public ResponseEntity<ApiResponse<String>> runCode(@RequestBody CodeDTO codeDTO) {
        String result = codeService.runCode(codeDTO);
        ApiResponse<String> response = new ApiResponse<>(200, true, "코드 테스트 성공", result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}