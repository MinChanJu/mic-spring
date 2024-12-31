package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.repository.ExampleRepository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {
    
    @Autowired
    private ExampleRepository exampleRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Example> getExamplesByProblemId(Integer problemId) {
        return exampleRepository.findByProblemId(problemId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Example createExample(Example example) {
        return exampleRepository.save(example);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteExample(Long id) {
        Example example = exampleRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
        exampleRepository.delete(example);
    }

}
