package com.example.mic_spring.controller;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.security.*;
import com.example.mic_spring.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;
import java.util.concurrent.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {

  private ExampleService exampleService;
  private RequestQueueService requestQueueService;
  private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

  public ExampleController(ExampleService exampleService, RequestQueueService requestQueueService) {
    this.exampleService = exampleService;
    this.requestQueueService = requestQueueService;
  }

  @GetMapping("/{problemId}")
  public ResponseEntity<ApiResponse<String>> getAllExamplesByProblemId(
      @PathVariable("problemId") Long problemId, HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<Example>>> future = requestQueueService.addRequest(() -> {
      List<Example> examples = exampleService.getAllExamplesByProblemId(problemId, token);
      return new ApiResponse<>(200, true, "예제 문제번호로 조회 성공", examples);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteContestById(@PathVariable("id") Long id,
      HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<Void>> future = requestQueueService.addRequest(() -> {
      exampleService.deleteExample(id, token);
      return new ApiResponse<>(200, true, "예제 삭제 성공", null);
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
