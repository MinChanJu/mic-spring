package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.model.UserDTO;
import com.example.my_gradle_spring_app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @PostMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{userId}")
    public UserDTO getUserDTOByUserId(@PathVariable String userId) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return null;
        } else {
            return new UserDTO(user.getId(), user.getName(), user.getUserId(), user.getEmail(), user.getAuthority(), user.getCreatedAt());
        }
    }

    @PostMapping("/{userId}/{userPw}")
    public User getUserByUserId(@PathVariable String userId, @PathVariable String userPw) {
        User user = userService.getUserByUserId(userId);
        if (user == null || !user.getUserPw().equals(userPw)) {
            user = new User();
            user.setId(-1L);
            user.setContestId(-1L);
            user.setAuthority(-1L);
        }
        return user;
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}