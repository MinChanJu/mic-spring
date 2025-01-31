package com.example.mic_spring.service;

import com.example.mic_spring.domain.dto.ApiResponse;

import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class RequestQueueService {
  private final BlockingQueue<Callable<ApiResponse<?>>> requestQueue = new LinkedBlockingQueue<>();
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  public RequestQueueService() {
    startQueueProcessor();
  }

  public <T> Future<ApiResponse<T>> addRequest(Callable<ApiResponse<T>> task) {
    CompletableFuture<ApiResponse<T>> future = new CompletableFuture<>();
    requestQueue.offer(() -> {
      try {
        future.complete(task.call());
      } catch (Exception e) {
        future.completeExceptionally(e);
      }
      return null;
    });
    return future;
  }

  private void startQueueProcessor() {
    executorService.submit(() -> {
      while (true) {
        try {
          Callable<ApiResponse<?>> task = requestQueue.take();
          task.call();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public ResponseEntity<ApiResponse<?>> getResult(Future<? extends ApiResponse<?>> future) {
    if (future == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(404, false, "요청 ID가 존재하지 않습니다.", null));
    }

    try {
      if (future.isDone()) {
        ApiResponse<?> response = future.get();
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.ok(new ApiResponse<>(202, true, "요청이 아직 처리 중입니다.", null));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>(500, false, "처리 중 오류 발생", null));
    }
  }
}