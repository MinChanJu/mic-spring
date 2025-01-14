package com.example.my_gradle_spring_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.my_gradle_spring_app.exception.CustomException;
import com.example.my_gradle_spring_app.exception.ErrorCode;
import com.example.my_gradle_spring_app.dto.ProblemDTO;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.repository.ProblemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired private ProblemRepository problemRepository;
    @Autowired private ExampleService exampleService;

    public Problem getProblemById(Long id) {
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

        if (!problemRepository.existsById(problem.getId())) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        for (Example example : examples) {
            exampleService.updateExample(example);
        }
        
        return problemRepository.save(problem);
    }

    public void deleteProblem(Long id) {
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        problemRepository.delete(problem.get());
    }
}
