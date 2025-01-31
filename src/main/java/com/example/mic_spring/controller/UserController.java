package com.example.mic_spring.controller;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.security.*;
import com.example.mic_spring.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private UserService userService;
  private RequestQueueService requestQueueService;
  private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

  public UserController(UserService userService, RequestQueueService requestQueueService) {
    this.userService = userService;
    this.requestQueueService = requestQueueService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<String>> getAllUsers(HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<User>>> future = requestQueueService.addRequest(() -> {
      List<User> users = userService.getAllUsers(token);
      return new ApiResponse<>(200, true, "모든 회원 조회 성공", users);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @GetMapping("/contest/{contestId}")
  public ResponseEntity<ApiResponse<String>> getAllUsersByContestId(@PathVariable("contestId") Long contestId,
      HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<List<User>>> future = requestQueueService.addRequest(() -> {
      List<User> users = userService.getAllUsersByContestId(contestId, token);
      return new ApiResponse<>(200, true, "모든 회원 조회 성공", users);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<String>> getUserDTOByUserId(@PathVariable("userId") String userId) {
    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<UserDTO>> future = requestQueueService.addRequest(() -> {
      UserDTO userDTO = userService.getUserByUserId(userId);
      return new ApiResponse<>(200, true, "회원 아이디로 조회 성공", userDTO);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<String>> getUserByUserIdAndUserPw(@RequestBody UserLoginDTO userLoginDTO) {
    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<UserResponseDTO>> future = requestQueueService.addRequest(() -> {
      UserResponseDTO userResponse = userService.getUserByUserIdAndUserPw(userLoginDTO);
      return new ApiResponse<>(200, true, "로그인 성공", userResponse);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<String>> createUser(@RequestBody User userDetail) {
    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<User>> future = requestQueueService.addRequest(() -> {
      User user = userService.createUser(userDetail);
      return new ApiResponse<>(200, true, "회원 생성 성공", user);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse<String>> updateUser(@RequestBody User userDetail, HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<User>> future = requestQueueService.addRequest(() -> {
      User user = userService.updateUser(userDetail, token);
      return new ApiResponse<>(200, true, "회원 수정 성공", user);
    });
    requestResults.put(requestId, future);

    return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("id") Long id, HttpServletRequest request) {
    Token token = (Token) request.getAttribute("token");

    String requestId = UUID.randomUUID().toString();
    Future<ApiResponse<Void>> future = requestQueueService.addRequest(() -> {
      userService.deleteUser(id, token);
      return new ApiResponse<>(200, true, "회원 삭제 성공", null);
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