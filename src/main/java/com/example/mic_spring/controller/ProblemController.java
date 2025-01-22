package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.dto.ProblemDTO;
import com.example.mic_spring.domain.dto.ProblemListDTO;
import com.example.mic_spring.domain.dto.ProblemScoreDTO;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.service.ProblemService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired private ProblemService problemService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProblemListDTO>>> getProblemList() {
        List<ProblemListDTO> problems = problemService.getProblemList();
        ApiResponse<List<ProblemListDTO>> response = new ApiResponse<>(200, true, "모든 문제 조회 성공", problems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<ApiResponse<List<ProblemListDTO>>> getProblemListWithUserId(@PathVariable("userId") String userId) {
        List<ProblemListDTO> problems = problemService.getProblemListWithUserId(userId);
        ApiResponse<List<ProblemListDTO>> response = new ApiResponse<>(200, true, "모든 문제 조회 성공", problems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<ApiResponse<List<ProblemListDTO>>> getProblemListByContestId(@PathVariable("contestId") Long contestId) {
        List<ProblemListDTO> problems = problemService.getProblemListByContestId(contestId);
        ApiResponse<List<ProblemListDTO>> response = new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/contest/{contestId}/{userId}")
    public ResponseEntity<ApiResponse<List<ProblemListDTO>>> getProblemListByContestIdWithUserId(@PathVariable("contestId") Long contestId, @PathVariable("userId") String userId) {
        List<ProblemListDTO> problems = problemService.getProblemListByContestIdWithUserId(contestId, userId);
        ApiResponse<List<ProblemListDTO>> response = new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Problem>> getProblemById(@PathVariable("id") Long id) {
        Problem problem = problemService.getProblemById(id);
        ApiResponse<Problem> response = new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Problem>>> getAllProblemsByUserId(@PathVariable("userId") String userId) {
        List<Problem> problems = problemService.getAllProblemsByUserId(userId);
        ApiResponse<List<Problem>> response = new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/solve/{userId}")
    public ResponseEntity<ApiResponse<List<ProblemScoreDTO>>> getAllSolveProblemsByUserId(@PathVariable("userId") String userId) {
        List<ProblemScoreDTO> problemScores = problemService.getAllSolveProblemsByUserId(userId);
        ApiResponse<List<ProblemScoreDTO>> response = new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problemScores);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Problem>> createProblem(@RequestBody ProblemDTO problemDTO) {
        Problem problem = problemService.createProblem(problemDTO);
        ApiResponse<Problem> response = new ApiResponse<Problem>(200, true, "문제 생성 성공", problem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Problem>> updateProblem(@RequestBody ProblemDTO problemDTO) {
        Problem problem = problemService.updateProblem(problemDTO);
        ApiResponse<Problem> response = new ApiResponse<Problem>(200, true, "문제 수정 성공", problem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProblem(@PathVariable("id") Long id) {
        problemService.deleteProblem(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "문제 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
