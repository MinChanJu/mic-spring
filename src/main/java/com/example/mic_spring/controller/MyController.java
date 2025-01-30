package com.example.mic_spring.controller;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.security.*;
import com.example.mic_spring.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;
import java.util.concurrent.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/data")
public class MyController {

    private CodeService codeService;
    private DataService dataService;
    private RequestQueueService requestQueueService;
    private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

    public MyController(CodeService codeService, DataService dataService, RequestQueueService requestQueueService) {
        this.codeService = codeService;
        this.dataService = dataService;
        this.requestQueueService = requestQueueService;
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<String>> getAllFilterContestsAndProblems(HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");

        String requestId = UUID.randomUUID().toString();
        Future<ApiResponse<ContestsAndProblemsDTO>> future = requestQueueService.addRequest(() -> {
            ContestsAndProblemsDTO contestsAndProblems = dataService.getAllFilterContestsAndProblems(token);
            return new ApiResponse<>(200, true, "대회 및 문제 조회 성공", contestsAndProblems);
        });
        requestResults.put(requestId, future);

        return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<String>> getScoreBoardByContestId(@PathVariable("contestId") Long contestId,
            HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");

        String requestId = UUID.randomUUID().toString();
        Future<ApiResponse<ScoreBoardDTO>> future = requestQueueService.addRequest(() -> {
            ScoreBoardDTO contestScores = dataService.getScoreBoardByContestId(contestId, token);
            return new ApiResponse<>(200, true, "대회 아이디로 스코어보드 조회 성공",
                    contestScores);
        });
        requestResults.put(requestId, future);

        return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
    }

    @PostMapping("/code")
    public ResponseEntity<ApiResponse<String>> runCode(@RequestBody CodeDTO codeDTO,
            HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");

        String requestId = UUID.randomUUID().toString();
        Future<ApiResponse<CodeResultDTO>> future = requestQueueService.addRequest(() -> {
            CodeResultDTO result = codeService.runCode(codeDTO, token);
            return new ApiResponse<>(200, true, "코드 테스트 성공", result);
        });
        requestResults.put(requestId, future);

        return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
    }

    @GetMapping("/result/{requestId}")
    public ResponseEntity<ApiResponse<?>> getResult(@PathVariable("requestId") String requestId) {
        Future<? extends ApiResponse<?>> future = requestResults.get(requestId);
        ResponseEntity<ApiResponse<?>> result = requestQueueService.getResult(future);
        ApiResponse<?> responseBody = Optional.ofNullable(result.getBody())
                .orElseGet(() -> new ApiResponse<>(502, false, "서버 내부 오류", null));
        if (responseBody.getStatus() == 200) {
            requestResults.remove(requestId);
        } else if (responseBody.getStatus() == 500) {
            requestResults.remove(requestId);
        }
        return result;
    }
}