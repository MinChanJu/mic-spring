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
@RequestMapping("/api/contests")
public class ContestController {

  private ContestService contestService;
  private RequestQueueService requestQueueService;
  private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

  public ContestController(ContestService contestService, RequestQueueService requestQueueService) {
    this.contestService = contestService;
    this.requestQueueService = requestQueueService;
  }

  @GetMapping("/all")
  public ResponseEntity<ApiResponse<String>> getContestList() {
    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<ContestListDTO>>> future = requestQueueService.addRequest(() -> {
      List<ContestListDTO> contests = contestService.getContestList();
      return new ApiResponse<>(200, true, "모든 대회 조회 성공", contests);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @GetMapping("/user")
  public ResponseEntity<ApiResponse<String>> getContestListForUserId(HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<ContestListDTO>>> future = requestQueueService.addRequest(() -> {
      List<ContestListDTO> contests = contestService.getContestListForUserId(token);
      return new ApiResponse<>(200, true, "회원 아이디로 조회 성공", contests);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> getContestById(@PathVariable("id") Long id) {
    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<ContestListDTO>>> future = requestQueueService.addRequest(() -> {
      List<ContestListDTO> contest = new ArrayList<>();
      contest.add(contestService.getContestById(id));
      return new ApiResponse<>(200, true, "대회 아이디로 조회 성공", contest);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<String>> createContest(@RequestBody Contest contestDetail,
      HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<Contest>> future = requestQueueService.addRequest(() -> {
      Contest contest = contestService.createContest(contestDetail, token);
      return new ApiResponse<>(200, true, "대회 생성 성공", contest);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse<String>> updateContest(@RequestBody Contest contestDetail,
      HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<Contest>> future = requestQueueService.addRequest(() -> {
      Contest contest = contestService.updateContest(contestDetail, token);
      return new ApiResponse<>(200, true, "대회 수정 성공", contest);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteContest(@PathVariable("id") Long id, HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<Void>> future = requestQueueService.addRequest(() -> {
      contestService.deleteContest(id, token);
      return new ApiResponse<>(200, true, "대회 삭제 성공", null);
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