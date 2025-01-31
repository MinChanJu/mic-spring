package com.example.mic_spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    request.setAttribute("token", new Token("", (short) 0, -1L));

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      if (!jwtUtil.isTokenExpired(token)) {
        String userId = jwtUtil.extractUserId(token);
        Short authority = jwtUtil.extractAuthority(token);
        Long contestId = jwtUtil.extractContestId(token);

        userId = (userId != null) ? userId : "";
        authority = (authority != null) ? authority : 0;
        contestId = (contestId != null) ? contestId : -1L;

        request.setAttribute("token", new Token(userId, authority, contestId));
      }
    }

    filterChain.doFilter(request, response);
  }
}