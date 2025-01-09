package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Solved;
import com.example.my_gradle_spring_app.model.SolvedDTO;
import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.model.UserDTO;
import com.example.my_gradle_spring_app.service.SolvedService;
import com.example.my_gradle_spring_app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private SolvedService solvedService;

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
            user.setContest(-1);
            user.setAuthority(-1);
        }
        return user;
    }

    @PostMapping("/solved")
    public Solved solvedProblem(@RequestBody SolvedDTO solvedDTO) {
        List<Solved> solveds = solvedService.getSolvedsByUserId(solvedDTO.getUserId());
        Solved newSolved = new Solved();
        newSolved.setUserId(solvedDTO.getUserId());
        newSolved.setProblemId(solvedDTO.getProblemId());
        newSolved.setScore(solvedDTO.getScore());
        for (Solved solved : solveds) {
            if (solved.getProblemId() == solvedDTO.getProblemId()) newSolved.setId(solved.getId());
        }
        return solvedService.createSolved(newSolved);
    }
    

    @PostMapping("/solved/{userId}")
    public List<Solved> getSolvedOfUser(@PathVariable String userId) {
        return solvedService.getSolvedsByUserId(userId);
    }

    @PostMapping("/solved/problem/{problemId}")
    public List<Solved> getSolvedOfUser(@PathVariable Long problemId) {
        return solvedService.getSolvedsByProblemId(problemId);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}