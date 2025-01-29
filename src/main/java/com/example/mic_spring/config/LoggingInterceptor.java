package com.example.mic_spring.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private double totalTraffic = 0;

    @SuppressWarnings("null")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws IOException {
        int requestSize = request.getContentLength();
        int responseSize = response.getBufferSize();

        totalTraffic += ((requestSize > 0 ? requestSize : 0) + responseSize) / (1024.0 * 1024.0);

        System.out.printf("누적 트래픽 크기: %.2f MB%n", totalTraffic);
    }
}