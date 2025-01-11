package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.repository.ExampleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {
    
    @Autowired private ExampleRepository exampleRepository;

    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

    public List<Example> getExamplesByProblemId(Long problemId) {
        return exampleRepository.findByProblemId(problemId);
    }

    public Example createExample(Example example) {
        return exampleRepository.save(example);
    }

    public Example updateExample(Example example) {
        return exampleRepository.save(example);
    }

    public void deleteExample(Long id) {
        Example example = exampleRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
        exampleRepository.delete(example);
    }

    public void deleteExampleByProblemId(Long problemId) {
        List<Example> examples = exampleRepository.findByProblemId(problemId);
        for (Example example : examples) {
            exampleRepository.delete(example);
        }
    }
}
