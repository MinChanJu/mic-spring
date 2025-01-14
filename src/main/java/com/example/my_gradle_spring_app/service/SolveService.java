package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Solve;
import com.example.my_gradle_spring_app.repository.SolveRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolveService {
    
    @Autowired private SolveRepository solveRepository;

    public List<Solve> getAllSolveds() {
        return solveRepository.findAll();
    }

    public List<Solve> getSolvedsByUserId(String userId) {
        return solveRepository.findByUserId(userId);
    }

    public List<Solve> getSolvedsByProblemId(Long problemId) {
        return solveRepository.findByProblemId(problemId);
    }

    public Solve getSolvedByUserIdAndProblemId(String userId, Long problemId) {
        return solveRepository.findByUserIdAndProblemId(userId, problemId).orElse(null);
    }

    public Solve createSolved(Solve solved) {
        return solveRepository.save(solved);
    }

    public Solve updateSolved(Solve solved) {
        return solveRepository.save(solved);
    }

    public void deleteSolved(Long id) {
        Solve solved = solveRepository.findById(id).orElseThrow(() -> new RuntimeException("solved not found"));
        solveRepository.delete(solved);
    }

    public void deleteSolvedByProblemId(Long problemId) {
        List<Solve> solveds = solveRepository.findByProblemId(problemId);
        for (Solve solved : solveds) {
            solveRepository.delete(solved);
        }
    }

    public void deleteSolvedByUserId(String userId) {
        List<Solve> solveds = solveRepository.findByUserId(userId);
        for (Solve solved : solveds) {
            solveRepository.delete(solved);
        }
    }
}
