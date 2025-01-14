package com.example.my_gradle_spring_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.my_gradle_spring_app.exception.CustomException;
import com.example.my_gradle_spring_app.exception.ErrorCode;
import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.repository.ExampleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExampleService {
    
    @Autowired private ExampleRepository exampleRepository;

    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

    public List<Example> getAllExamplesByProblemId(Long problemId) {
        return exampleRepository.findByProblemId(problemId);
    }

    public Example createExample(Example example) {
        return exampleRepository.save(example);
    }

    public Example updateExample(Example example) {
        if (!exampleRepository.existsById(example.getId())) return createExample(example);

        return exampleRepository.save(example);
    }

    public void deleteExample(Long id) {
        Optional<Example> example = exampleRepository.findById(id);
        if (example.isEmpty()) throw new CustomException(ErrorCode.EXAMPLE_NOT_FOUND);

        exampleRepository.delete(example.get());
    }

    public void deleteAllExamplesByProblemId(Long problemId) {
        List<Example> examples = exampleRepository.findByProblemId(problemId);
        for (Example example : examples) {
            exampleRepository.delete(example);
        }
    }
}
