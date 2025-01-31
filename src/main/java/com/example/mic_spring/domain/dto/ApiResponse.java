package com.example.mic_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
  private int status;
  private boolean success;
  private String message;
  private T data;
}
