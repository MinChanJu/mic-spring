package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.repository.ProblemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired private ProblemRepository problemRepository;

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Optional<Problem> getProblemById(Long id) {
        return problemRepository.findById(id);
    }

    public List<Problem> getProblemByContestId(Long contestId) {
        return problemRepository.findByContestId(contestId);
    }

    public Problem createProblem(Problem problem) {
        if (problemRepository.existsByProblemName(problem.getProblemName())) {
            Problem sub = new Problem();
            sub.setId(-1L);
            return sub;
        }
        return problemRepository.save(problem);
    }

    public Problem updateProblem(Problem problem) {
        if (problemRepository.existsById(problem.getId())) return problemRepository.save(problem);

        Problem sub = new Problem();
        sub.setId(-1L);
        return sub;
    }

    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
        problemRepository.delete(problem);
    }
}
