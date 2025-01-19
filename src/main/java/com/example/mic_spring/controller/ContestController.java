package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.entity.Contest;
import com.example.mic_spring.service.ContestService;

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
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired private ContestService contestService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Contest>>> getAllContests() {
        List<Contest> contests = contestService.getAllContests();
        ApiResponse<List<Contest>> response = new ApiResponse<>(200, true, "모든 대회 조회 성공", contests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Contest>> getContestById(@PathVariable("id") Long id) {
        Contest contest = contestService.getContestById(id);
        ApiResponse<Contest> response = new ApiResponse<>(200, true, "대회 아이디로 조회 성공", contest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Contest>>> getAllContestsByUserId(@PathVariable("userId") String userId) {
        List<Contest> contests = contestService.getAllContestsByUserId(userId);
        ApiResponse<List<Contest>> response = new ApiResponse<>(200, true, "대회 아이디로 조회 성공", contests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Contest>> createContest(@RequestBody Contest contestDetail) {
        Contest contest = contestService.createContest(contestDetail);
        ApiResponse<Contest> response = new ApiResponse<>(200, true, "대회 생성 성공", contest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Contest>> updateContest(@RequestBody Contest contestDetail) {
        Contest contest = contestService.updateContest(contestDetail);
        ApiResponse<Contest> response = new ApiResponse<>(200, true, "대회 수정 성공", contest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContest(@PathVariable("id") Long id) {
        contestService.deleteContest(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "대회 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}