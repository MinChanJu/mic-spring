package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.UserDTO;
import com.example.mic_spring.domain.dto.UserLoginDTO;
import com.example.mic_spring.domain.entity.User;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public User getUserById(Long id) {
        if (id == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return user.get();
    }

    public UserDTO getUserByUserId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        UserDTO userDTO = new UserDTO(user.get());
        return userDTO;
    }

    public User getUserByUserIdAndUserPw(UserLoginDTO userLoginDTO) {
        String userId = userLoginDTO.getUserId(), userPw = userLoginDTO.getUserPw();
        Optional<User> user = userRepository.findByUserIdAndUserPw(userId, userPw);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return user.get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByContestId(Long contestId) {
        if (contestId == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        
        return userRepository.findByContestId(contestId);
    }

    public User createUser(User user) {
        if (userRepository.existsByUserId(user.getUserId())) throw new CustomException(ErrorCode.DUPLICATE_USER_ID);

        String password = user.getUserPw();
        if (!password.matches(".*[a-zA-Z].*")) throw new CustomException(ErrorCode.INVALID_PASSWORD_ALP);   // 영문자 포함 되었는지 확인
        if (!password.matches(".*\\d.*")) throw new CustomException(ErrorCode.INVALID_PASSWORD_NUM);        // 숫자 포함 되었는지 확인
        if (!password.matches("[a-zA-Z0-9]+")) throw new CustomException(ErrorCode.INVALID_PASSWORD_SPE);   // 영문자 및 숫자를 제외한 다른 문자가 포함 안되었는지 확인
        if (password.length() < 8) throw new CustomException(ErrorCode.INVALID_PASSWORD_LEN);                     // 길이가 8자 이상인지 확인

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (user.getId() == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        if (!userRepository.existsById(user.getId())) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        
        userRepository.delete(user.get());
    }
}