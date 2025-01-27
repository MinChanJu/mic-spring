package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.entity.Example;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.ExampleRepository;
import com.example.mic_spring.repository.ProblemRepository;
import com.example.mic_spring.security.Token;

import java.util.List;
import java.util.Optional;

@Service
public class ExampleService {

    @Autowired
    private ExampleRepository exampleRepository;
    @Autowired
    private ProblemRepository problemRepository;

    public List<Example> getAllExamples(Token token) {
        if (token.getAuthority() != 5) throw new CustomException(ErrorCode.UNAUTHORIZED);
        return exampleRepository.findAll();
    }

    public List<Example> getAllExamplesByProblemId(Long problemId, Token token) {
        Optional<Problem> findProblem = problemRepository.findById(problemId);
        if (findProblem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Problem problem = findProblem.get();
        if (!problem.getUserId().equals(token.getUserId()) && token.getAuthority() != 5)
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        return exampleRepository.findByProblemId(problemId);
    }

    public List<Example> getAllExamplesByProblemId(Long problemId) {
        return exampleRepository.findByProblemId(problemId);
    }

    public Example createExample(Example example) {
        return exampleRepository.save(example);
    }

    public Example updateExample(Example example) {
        if (example.getId() == null)
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        if (!exampleRepository.existsById(example.getId()))
            return createExample(example);

        return exampleRepository.save(example);
    }

    public void deleteExample(Long id, Token token) {
        if (id == null)
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        Optional<Example> findExample = exampleRepository.findById(id);
        if (findExample.isEmpty())
            throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);
        Example example = findExample.get();
        if (!example.getUserId().equals(token.getUserId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        exampleRepository.delete(example);
    }

    public void deleteAllExamplesByProblemId(Long problemId) {
        List<Example> examples = exampleRepository.findByProblemId(problemId);
        for (Example example : examples) {
            exampleRepository.delete(example);
        }
    }
}
