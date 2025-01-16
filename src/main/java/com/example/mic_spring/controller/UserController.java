package com.example.mic_spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mic_spring.domain.dto.ApiResponse;
import com.example.mic_spring.domain.dto.UserDTO;
import com.example.mic_spring.domain.entity.User;
import com.example.mic_spring.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ApiResponse<List<User>> response = new ApiResponse<>(200, true, "모든 회원 조회 성공", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDTOByUserId(@PathVariable String userId) {
        UserDTO userDTO = userService.getUserByUserId(userId);
        ApiResponse<UserDTO> response = new ApiResponse<>(200, true, "회원 아이디로 조회 성공", userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{userId}/{userPw}")
    public ResponseEntity<ApiResponse<User>> getUserByUserId(@PathVariable String userId, @PathVariable String userPw) {
        User user = userService.getUserByUserIdAndUserPw(userId, userPw);
        ApiResponse<User> response = new ApiResponse<>(200, true, "로그인 성공", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User userDetail) {
        User user = userService.createUser(userDetail);
        ApiResponse<User> response = new ApiResponse<>(200, true, "회원 생성 성공", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>>  updateUser(@RequestBody User userDetail) {
        User user = userService.updateUser(userDetail);
        ApiResponse<User> response = new ApiResponse<>(200, true, "회원 수정 성공", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>(200, true, "회원 삭제 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}