package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.ProblemDTO;
import com.example.mic_spring.domain.entity.Example;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.ProblemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired private ProblemRepository problemRepository;
    @Autowired private ExampleService exampleService;

    public Problem getProblemById(Long id) {
        if (id == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        return problem.get();
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public List<Problem> getAllProblemsByContestId(Long contestId) {
        return problemRepository.findByContestId(contestId);
    }

    public Problem createProblem(ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (problemRepository.existsByProblemName(problem.getProblemName())) throw new CustomException(ErrorCode.DUPLICATE_PROBLEM_NAME);

        Problem curProblem = problemRepository.save(problem);

        for (Example example : examples) {
            example.setProblemId(curProblem.getId());
            exampleService.createExample(example);
        }

        return curProblem;
    }

    public Problem updateProblem(ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (problem.getId() == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        if (!problemRepository.existsById(problem.getId())) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        for (Example example : examples) {
            exampleService.updateExample(example);
        }
        
        return problemRepository.save(problem);
    }

    public void deleteProblem(Long id) {
        if (id == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        problemRepository.delete(problem.get());
    }
}
