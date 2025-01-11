package com.example.my_gradle_spring_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.service.ExampleService;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {

    @Autowired private ExampleService exampleService;

    @PostMapping("/{problemId}")
    public List<Example> getProblemExamples(@PathVariable Long problemId) {
        return exampleService.getExamplesByProblemId(problemId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExample(@PathVariable Long id) {
        exampleService.deleteExample(id);
        return ResponseEntity.noContent().build();
    }
}
