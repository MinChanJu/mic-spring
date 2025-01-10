package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.model.ExampleDTO;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.ProblemDTO;
import com.example.my_gradle_spring_app.repository.ExampleRepository;
import com.example.my_gradle_spring_app.repository.ProblemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
    
    @Autowired private ProblemRepository problemRepository;
    @Autowired private ExampleRepository exampleRepository;
    @Autowired private ExampleService exampleService;

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
        return problemRepository.save(problem);
    }

    public Problem updateProblem(Long id, ProblemDTO problemDetails) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));

        problem.setProblemName(problemDetails.getProblemName());
        problem.setProblemDescription(problemDetails.getProblemDescription());
        problem.setProblemInputDescription(problemDetails.getProblemInputDescription());
        problem.setProblemOutputDescription(problemDetails.getProblemOutputDescription());
        problem.setProblemExampleInput(problemDetails.getProblemExampleInput());
        problem.setProblemExampleOutput(problemDetails.getProblemExampleOutput());
        
        List<Example> examples = exampleRepository.findByProblemId(id);
        for (Example example : examples) {
            exampleService.deleteExample(example.getId());
        }

        for (ExampleDTO exampleDTO : problemDetails.getExamples()) {
            Example example = new Example();
            example.setExampleInput(exampleDTO.getExampleInput());
            example.setExampleOutput(exampleDTO.getExampleOutput());
            example.setProblemId(id);
            exampleService.createExample(example);
        }

        return problemRepository.save(problem);
    }

    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
        problemRepository.delete(problem);
        List<Example> examples = exampleRepository.findByProblemId(id);
        for (Example example : examples) {
            exampleService.deleteExample(example.getId());
        }
    }
}
