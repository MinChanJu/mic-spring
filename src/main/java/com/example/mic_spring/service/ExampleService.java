package com.example.mic_spring.service;

import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.exception.*;
import com.example.mic_spring.repository.*;
import com.example.mic_spring.security.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExampleService {

    private ExampleRepository exampleRepository;
    private ProblemRepository problemRepository;

    public ExampleService(ExampleRepository exampleRepository, ProblemRepository problemRepository) {
        this.exampleRepository = exampleRepository;
        this.problemRepository = problemRepository;
    }

    @Transactional(readOnly = true)
    public List<Example> getAllExamples(Token token) {
        if (token.getAuthority() != 5)
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        return exampleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Example> getAllExamplesByProblemId(Long problemId, Token token) {
        Optional<Problem> findProblem = problemRepository.findById(problemId);
        if (findProblem.isEmpty())
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Problem problem = findProblem.get();
        if (!problem.getUserId().equals(token.getUserId()) && token.getAuthority() != 5)
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        return exampleRepository.findByProblemId(problemId);
    }

    @Transactional(readOnly = true)
    public List<Example> getAllExamplesByProblemId(Long problemId) {
        return exampleRepository.findByProblemId(problemId);
    }

    @Transactional
    public Example createExample(Example example) {
        return exampleRepository.save(example);
    }

    @Transactional
    public Example updateExample(Example example) {
        if (example.getId() == null)
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        if (!exampleRepository.existsById(example.getId()))
            return createExample(example);

        return exampleRepository.save(example);
    }

    @Transactional
    public void deleteExample(Long id, Token token) {
        if (id == null)
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        Optional<Example> findExample = exampleRepository.findById(id);
        if (findExample.isEmpty())
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        Example example = findExample.get();
        if (!example.getUserId().equals(token.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        exampleRepository.delete(example);
    }

    @Transactional
    public void deleteAllExamplesByProblemId(Long problemId) {
        List<Example> examples = exampleRepository.findByProblemId(problemId);
        for (Example example : examples) {
            exampleRepository.delete(example);
        }
    }
}
