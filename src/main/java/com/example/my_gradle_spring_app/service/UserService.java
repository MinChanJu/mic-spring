package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public static boolean isPasswordValid(String password) {
        if (Pattern.compile("[\\s'\";]+").matcher(password).find()) return false;
        if (Pattern.compile("\\p{Cntrl}").matcher(password).find()) return false;
        if (!password.matches(".*[a-zA-Z]+.*")) return false;
        if (!password.matches(".*[0-9]+.*")) return false;
        if (password.length() < 8) return false;
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUserByContestId(Long contestId) {
        return userRepository.findByContestId(contestId);
    }

    public User getUserByUserId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        return user.orElse(null);
    }

    public User createUser(User user) {
        if (userRepository.existsByUserId(user.getUserId())) {
            return null;
        } else {
            if (isPasswordValid(user.getUserPw())) {
                return userRepository.save(user);
            }
            return null;
        }
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}