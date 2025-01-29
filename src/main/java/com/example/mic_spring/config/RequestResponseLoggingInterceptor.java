package com.example.mic_spring.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    @SuppressWarnings("null")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        int requestSize = request.getContentLength();
        int responseSize = response.getBufferSize();

        System.out.println("요청 크기: " + requestSize + " bytes");
        System.out.println("응답 크기: " + responseSize + " bytes");
    }
}