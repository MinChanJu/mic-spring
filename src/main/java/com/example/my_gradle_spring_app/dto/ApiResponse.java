package com.example.my_gradle_spring_app.dto;

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
