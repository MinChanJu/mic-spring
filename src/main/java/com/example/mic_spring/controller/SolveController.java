package com.example.mic_spring.controller;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/solves")
public class SolveController {
    
    private SolveService solveService;
    private RequestQueueService requestQueueService;
    private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

    public SolveController(SolveService solveService, RequestQueueService requestQueueService) {
        this.solveService = solveService;
        this.requestQueueService = requestQueueService;
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<String>> getAllSolvesByUserId(@PathVariable("userId") String userId) {
        String requestId = UUID.randomUUID().toString();
        Future<ApiResponse<List<Solve>>> future = requestQueueService.addRequest(() -> {
            List<Solve> solves = solveService.getAllSolvesByUserId(userId);
            return new ApiResponse<>(200, true, "해결 성공", solves);
        });
        requestResults.put(requestId, future);

        return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<ApiResponse<String>> getAllSolvesByProblemId(@PathVariable("problemId") Long problemId) {
        String requestId = UUID.randomUUID().toString();
        Future<ApiResponse<List<Solve>>> future = requestQueueService.addRequest(() -> {
            List<Solve> solves = solveService.getAllSolvesByProblemId(problemId);
            return new ApiResponse<>(200, true, "해결 성공", solves);
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
