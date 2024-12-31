package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.model.UserDTO;
import com.example.my_gradle_spring_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{userId}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserDTO getUserDTOByUserId(@PathVariable String userId) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return null;
        } else {
            return new UserDTO(user.getId(), user.getName(), user.getUserId(), user.getEmail(), user.getAuthority(), user.getCreatedAt());
        }
    }

    @PostMapping("/{userId}/{userPw}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User getUserByUserId(@PathVariable String userId, @PathVariable String userPw) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return null;
        } else if (user.getUserPw().equals(userPw)) {
            return user;
        } else {
            return null;
        }
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}