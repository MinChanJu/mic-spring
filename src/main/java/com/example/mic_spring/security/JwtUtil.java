package com.example.mic_spring.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

	@Value("${JWT_SECRET}")
	private String secretKey;
	private long expirationTime = 1000L * 60 * 60 * 24 * 7; // 7일

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	// JWT 토큰 생성 (Short 타입의 권한 포함)
	public String generateToken(String userId, Short authority, Long contestId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("authority", authority); // 권한 저장
		claims.put("contestId", contestId); // 대회 아이디 저장

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userId)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	// 토큰에서 클레임 추출
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	// 토큰에서 사용자 이름 추출
	public String extractUserId(String token) {
		return extractClaims(token).getSubject();
	}

	// 토큰에서 Short 타입의 권한 추출
	public Short extractAuthority(String token) {
		return Short.valueOf(extractClaims(token).get("authority").toString());
	}

	// 토큰에서 Short 타입의 권한 추출
	public Long extractContestId(String token) {
		return Long.valueOf(extractClaims(token).get("contestId").toString());
	}

	// 토큰 만료 여부 확인
	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token, String userId) {
		return extractUserId(token).equals(userId) && !isTokenExpired(token);
	}
}