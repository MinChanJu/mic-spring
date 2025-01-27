package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.dto.UserDTO;
import com.example.mic_spring.domain.dto.UserLoginDTO;
import com.example.mic_spring.domain.dto.UserResponseDTO;
import com.example.mic_spring.domain.entity.User;
import com.example.mic_spring.security.Token;
import com.example.mic_spring.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");
        List<User> users = userService.getAllUsers(token);
        ApiResponse<List<User>> response = new ApiResponse<>(200, true, "모든 회원 조회 성공", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDTOByUserId(@PathVariable("userId") String userId) {
        UserDTO userDTO = userService.getUserByUserId(userId);
        ApiResponse<UserDTO> response = new ApiResponse<>(200, true, "회원 아이디로 조회 성공", userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsersByContestId(@PathVariable("contestId") Long contestId, HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");
        List<User> users = userService.getAllUsersByContestId(contestId, token);
        ApiResponse<List<User>> response = new ApiResponse<>(200, true, "회원 아이디로 조회 성공", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByUserIdAndUserPw(@RequestBody UserLoginDTO userLoginDTO) {
        UserResponseDTO userResponse = userService.getUserByUserIdAndUserPw(userLoginDTO);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(200, true, "로그인 성공", userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User userDetail) {
        User user = userService.createUser(userDetail);
        ApiResponse<User> response = new ApiResponse<>(200, true, "회원 생성 성공", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>>  updateUser(@RequestBody User userDetail, HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");
        User user = userService.updateUser(userDetail, token);
        ApiResponse<User> response = new ApiResponse<>(200, true, "회원 수정 성공", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") Long id, HttpServletRequest request) {
        Token token = (Token) request.getAttribute("token");
        userService.deleteUser(id, token);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "회원 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}