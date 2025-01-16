package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.entity.Solve;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.SolveRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SolveService {
    
    @Autowired private SolveRepository solveRepository;

    public Solve getSolveByUserIdAndProblemId(String userId, Long problemId) {
        if (problemId == null) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
        Optional<Solve> solve = solveRepository.findByUserIdAndProblemId(userId, problemId);
        if (solve.isEmpty()) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);

        return solve.get();
    }

    public List<Solve> getAllSolves() {
        return solveRepository.findAll();
    }

    public List<Solve> getAllSolvesByUserId(String userId) {
        return solveRepository.findByUserId(userId);
    }

    public List<Solve> getAllSolvesByProblemId(Long problemId) {
        return solveRepository.findByProblemId(problemId);
    }

    public Solve solveProblem(Solve solveDetail) {
        if (solveDetail.getProblemId() == null) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
        Optional<Solve> solve = solveRepository.findByUserIdAndProblemId(solveDetail.getUserId(), solveDetail.getProblemId());
        if (solve.isEmpty()) return solveRepository.save(solveDetail);

        Solve findSolve = solve.get();
        if (findSolve.getScore() > solveDetail.getScore()) return findSolve;

        solveDetail.setId(findSolve.getId());
        return solveRepository.save(solveDetail);
    }

    public void deleteSolve(Long id) {
        if (id == null) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
        Optional<Solve> solve = solveRepository.findById(id);
        if (solve.isEmpty()) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);

        solveRepository.delete(solve.get());
    }

    public void deleteAllSolvesByProblemId(Long problemId) {
        if (problemId == null) throw new CustomException(ErrorCode.SOLVE_NOT_FOUND);
        List<Solve> solves = solveRepository.findByProblemId(problemId);
        for (Solve solve : solves) {
            solveRepository.delete(solve);
        }
    }

    public void deleteAllSolvesByUserId(String userId) {
        List<Solve> solves = solveRepository.findByUserId(userId);
        for (Solve solve : solves) {
            solveRepository.delete(solve);
        }
    }
}
