package com.example.mic_spring.service;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.exception.*;
import com.example.mic_spring.repository.*;
import com.example.mic_spring.security.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

  private UserRepository userRepository;
  private ContestService contestService;
  private JwtUtil jwtUtil;

  public UserService(UserRepository userRepository, ContestService contestService, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.contestService = contestService;
    this.jwtUtil = jwtUtil;
  }

  @Transactional(readOnly = true)
  public User getUserById(Long id) {
    if (id == null)
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty())
      throw new CustomException(ErrorCode.USER_NOT_FOUND);

    return user.get();
  }

  @Transactional(readOnly = true)
  public UserDTO getUserByUserId(String userId) {
    Optional<User> user = userRepository.findByUserId(userId);
    if (user.isEmpty())
      throw new CustomException(ErrorCode.USER_NOT_FOUND);

    UserDTO userDTO = new UserDTO(user.get());
    return userDTO;
  }

  @Transactional(readOnly = true)
  public UserResponseDTO getUserByUserIdAndUserPw(UserLoginDTO userLoginDTO) {
    String userId = userLoginDTO.getUserId(), userPw = userLoginDTO.getUserPw();
    Optional<User> findUser = userRepository.findByUserIdAndUserPw(userId, userPw);
    if (findUser.isEmpty())
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    User user = findUser.get();

    return new UserResponseDTO(user, jwtUtil.generateToken(userId, user.getAuthority(), user.getContestId()));
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers(Token token) {
    if (token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsersByContestId(Long contestId, Token token) {
    if (token.getUserId() == null)
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    if (token.getAuthority() == null)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    ContestListDTO contest = contestService.getContestById(contestId);
    if (!token.getUserId().equals(contest.getUserId()) && token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    return userRepository.findByContestId(contestId);
  }

  @Transactional(readOnly = true)
  public List<UserDTO> getAllUserDTOsByContestId(Long contestId) {
    List<User> users = userRepository.findByContestId(contestId);
    List<UserDTO> userDTOs = new ArrayList<>();
    for (User user : users) {
      userDTOs.add(new UserDTO(user));
    }
    return userDTOs;
  }

  @Transactional
  public User createUser(User user) {
    if (userRepository.existsByUserId(user.getUserId()))
      throw new CustomException(ErrorCode.DUPLICATE_USER_ID);

    String password = user.getUserPw();
    if (!password.matches(".*[a-zA-Z].*"))
      throw new CustomException(ErrorCode.INVALID_PASSWORD_ALP); // 영문자 포함 되었는지 확인
    if (!password.matches(".*\\d.*"))
      throw new CustomException(ErrorCode.INVALID_PASSWORD_NUM); // 숫자 포함 되었는지 확인
    if (!password.matches("[a-zA-Z0-9]+"))
      throw new CustomException(ErrorCode.INVALID_PASSWORD_SPE); // 영문자 및 숫자를 제외한 다른 문자가 포함 안되었는지 확인
    if (password.length() < 8)
      throw new CustomException(ErrorCode.INVALID_PASSWORD_LEN); // 길이가 8자 이상인지 확인

    return userRepository.save(user);
  }

  @Transactional
  public User updateUser(User user, Token token) {
    if (user.getId() == null)
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    Optional<User> findUser = userRepository.findById(user.getId());
    if (findUser.isEmpty())
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    User prev = findUser.get();

    if (!user.getUserId().equals(token.getUserId()) && token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    if (user.getUserId().equals(token.getUserId()))
      return userRepository.save(user);
    if (!prev.updateAdmin(user))
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    return userRepository.save(user);
  }

  @Transactional
  public void deleteUser(Long id, Token token) {
    if (id == null)
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    Optional<User> user = userRepository.findById(id);
    if (user.isEmpty())
      throw new CustomException(ErrorCode.USER_NOT_FOUND);

    if (!token.getUserId().equals(user.get().getUserId()) && token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    userRepository.delete(user.get());
  }
}