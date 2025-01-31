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
@RequestMapping("/api/problems")
public class ProblemController {

	private ProblemService problemService;
	private RequestQueueService requestQueueService;
	private final Map<String, Future<? extends ApiResponse<?>>> requestResults = new ConcurrentHashMap<>();

	public ProblemController(ProblemService problemService, RequestQueueService requestQueueService) {
		this.problemService = problemService;
		this.requestQueueService = requestQueueService;
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<String>> getProblemList(HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<List<ProblemListDTO>>> future = requestQueueService.addRequest(() -> {
			List<ProblemListDTO> problems = problemService.getProblemList(token);
			return new ApiResponse<>(200, true, "모든 문제 조회 성공", problems);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@GetMapping("/contest/{contestId}")
	public ResponseEntity<ApiResponse<String>> getProblemListByContestId(@PathVariable("contestId") Long contestId,
			HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<List<ProblemListDTO>>> future = requestQueueService.addRequest(() -> {
			List<ProblemListDTO> problems = problemService.getProblemListByContestId(contestId, token);
			return new ApiResponse<>(200, true, "모든 문제 조회 성공", problems);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> getProblemById(@PathVariable("id") Long id, HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<ProblemScoreDTO>> future = requestQueueService.addRequest(() -> {
			ProblemScoreDTO problem = problemService.getProblemById(id, token);
			return new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problem);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<String>> getAllProblemsByUserId(@PathVariable("userId") String userId,
			HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<List<Problem>>> future = requestQueueService.addRequest(() -> {
			List<Problem> problems = problemService.getAllProblemsByUserId(userId, token);
			return new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problems);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@GetMapping("/solve/{userId}")
	public ResponseEntity<ApiResponse<String>> getAllSolveProblemsByUserId(
			@PathVariable("userId") String userId, HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<List<ProblemScoreDTO>>> future = requestQueueService.addRequest(() -> {
			List<ProblemScoreDTO> problemScores = problemService.getAllSolveProblemsByUserId(userId, token);
			return new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problemScores);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));

	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<String>> createProblem(@RequestBody ProblemDTO problemDTO,
			HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<Problem>> future = requestQueueService.addRequest(() -> {
			Problem problem = problemService.createProblem(problemDTO, token);
			return new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problem);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<String>> updateProblem(@RequestBody ProblemDTO problemDTO,
			HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<Problem>> future = requestQueueService.addRequest(() -> {
			Problem problem = problemService.updateProblem(problemDTO, token);
			return new ApiResponse<>(200, true, "문제 아이디로 조회 성공", problem);
		});
		requestResults.put(requestId, future);

		return ResponseEntity.ok(new ApiResponse<>(200, true, "요청이 처리 중입니다.", requestId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteProblem(@PathVariable("id") Long id, HttpServletRequest request) {
		Token token = (Token) request.getAttribute("token");

		String requestId = UUID.randomUUID().toString();
		Future<ApiResponse<Void>> future = requestQueueService.addRequest(() -> {
			problemService.deleteProblem(id, token);
			return new ApiResponse<>(200, true, "문제 삭제 성공", null);
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
