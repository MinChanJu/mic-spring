package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Solved;
import com.example.my_gradle_spring_app.repository.SolvedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolvedService {
    
    @Autowired private SolvedRepository solvedRepository;

    public List<Solved> getAllSolveds() {
        return solvedRepository.findAll();
    }

    public List<Solved> getSolvedsByUserId(String userId) {
        return solvedRepository.findByUserId(userId);
    }

    public List<Solved> getSolvedsByProblemId(Long problemId) {
        return solvedRepository.findByProblemId(problemId);
    }

    public Solved getSolvedByUserIdAndProblemId(String userId, Long problemId) {
        return solvedRepository.findByUserIdAndProblemId(userId, problemId).orElse(null);
    }

    public Solved createSolved(Solved solved) {
        return solvedRepository.save(solved);
    }

    public Solved updateSolved(Solved solved) {
        return solvedRepository.save(solved);
    }

    public void deleteSolved(Long id) {
        Solved solved = solvedRepository.findById(id).orElseThrow(() -> new RuntimeException("solved not found"));
        solvedRepository.delete(solved);
    }

    public void deleteSolvedByProblemId(Long problemId) {
        List<Solved> solveds = solvedRepository.findByProblemId(problemId);
        for (Solved solved : solveds) {
            solvedRepository.delete(solved);
        }
    }

    public void deleteSolvedByUserId(String userId) {
        List<Solved> solveds = solvedRepository.findByUserId(userId);
        for (Solved solved : solveds) {
            solvedRepository.delete(solved);
        }
    }
}
