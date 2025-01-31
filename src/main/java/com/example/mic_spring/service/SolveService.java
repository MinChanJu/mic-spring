package com.example.mic_spring.service;

import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.exception.*;
import com.example.mic_spring.repository.*;
import com.example.mic_spring.security.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SolveService {

  private SolveRepository solveRepository;

  public SolveService(SolveRepository solveRepository) {
    this.solveRepository = solveRepository;
  }

  @Transactional(readOnly = true)
  public Solve getSolveByUserIdAndProblemId(String userId, Long problemId) {
    if (userId == null || problemId == null)
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
    Optional<Solve> solve = solveRepository.findByUserIdAndProblemId(userId, problemId);
    if (solve.isEmpty())
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);

    return solve.get();
  }

  @Transactional(readOnly = true)
  public Solve existSolveByUserIdAndProblemId(String userId, Long problemId) {
    if (userId == null || problemId == null)
      return null;
    Optional<Solve> solve = solveRepository.findByUserIdAndProblemId(userId, problemId);
    if (solve.isEmpty())
      return null;

    return solve.get();
  }

  @Transactional(readOnly = true)
  public List<Solve> getAllSolves() {
    return solveRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Solve> getAllSolvesByUserId(String userId) {
    return solveRepository.findByUserId(userId);
  }

  @Transactional(readOnly = true)
  public List<Solve> getAllSolvesByUserIdOrderByProblemIdAsc(String userId) {
    return solveRepository.findByUserIdOrderByProblemIdAsc(userId);
  }

  @Transactional(readOnly = true)
  public List<Solve> getAllSolvesByProblemId(Long problemId) {
    return solveRepository.findByProblemId(problemId);
  }

  @Transactional
  public Solve solveProblem(Solve solveDetail, Token token) {
    if (!solveDetail.getUserId().equals(token.getUserId()))
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    if (solveDetail.getProblemId() == null)
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
    Optional<Solve> solve = solveRepository.findByUserIdAndProblemId(solveDetail.getUserId(),
        solveDetail.getProblemId());
    if (solve.isEmpty())
      return solveRepository.save(solveDetail);

    Solve findSolve = solve.get();
    if (findSolve.getScore() > solveDetail.getScore())
      return findSolve;

    solveDetail.setId(findSolve.getId());
    return solveRepository.save(solveDetail);
  }

  @Transactional
  public void deleteSolve(Long id) {
    if (id == null)
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
    Optional<Solve> solve = solveRepository.findById(id);
    if (solve.isEmpty())
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);

    solveRepository.delete(solve.get());
  }

  @Transactional
  public void deleteAllSolvesByProblemId(Long problemId) {
    if (problemId == null)
      throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
    List<Solve> solves = solveRepository.findByProblemId(problemId);
    for (Solve solve : solves) {
      solveRepository.delete(solve);
    }
  }

  @Transactional
  public void deleteAllSolvesByUserId(String userId) {
    List<Solve> solves = solveRepository.findByUserId(userId);
    for (Solve solve : solves) {
      solveRepository.delete(solve);
    }
  }
}
