package com.example.my_gradle_spring_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.my_gradle_spring_app.dto.ApiResponse;
import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.service.ContestService;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired private ContestService contestService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<Contest>>> getAllContests() {
        List<Contest> contests = contestService.getAllContests();
        ApiResponse<List<Contest>> response = new ApiResponse<>(200, true, "모든 대회 조회 성공", contests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Contest>> getContestById(@PathVariable Long id) {
        Contest contest = contestService.getContestById(id);
        ApiResponse<Contest> response = new ApiResponse<>(200, true, "대회 아이디로 조회 성공", contest);
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
    public ResponseEntity<ApiResponse<Void>> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "대회 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}