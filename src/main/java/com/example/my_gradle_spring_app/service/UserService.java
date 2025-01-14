package com.example.my_gradle_spring_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.my_gradle_spring_app.exception.CustomException;
import com.example.my_gradle_spring_app.exception.ErrorCode;
import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return user.get();
    }

    public User getUserByUserId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return user.get();
    }

    public User getUserByUserIdAndUserPw(String userId, String userPw) {
        Optional<User> user = userRepository.findByUserIdAndUserPw(userId, userPw);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return user.get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByContestId(Long contestId) {
        return userRepository.findByContestId(contestId);
    }

    public User createUser(User user) {
        if (userRepository.existsByUserId(user.getUserId())) throw new CustomException(ErrorCode.DUPLICATE_USER_ID);

        String password = user.getUserPw();
        if (!password.matches(".*[a-zA-Z].*")) throw new CustomException(ErrorCode.INVALID_PASSWORD_ALP);
        if (!password.matches(".*\\d.*")) throw new CustomException(ErrorCode.INVALID_PASSWORD_NUM);
        if (!password.matches("[a-zA-Z0-9]+")) throw new CustomException(ErrorCode.INVALID_PASSWORD_SPE);
        if (password.length() < 8) throw new CustomException(ErrorCode.INVALID_PASSWORD_LEN);

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        
        userRepository.delete(user.get());
    }
}