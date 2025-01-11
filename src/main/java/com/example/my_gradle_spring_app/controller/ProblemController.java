package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.ProblemDTO;
import com.example.my_gradle_spring_app.service.ExampleService;
import com.example.my_gradle_spring_app.service.ProblemService;
import com.example.my_gradle_spring_app.service.SolvedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired private ProblemService problemService;
    @Autowired private ExampleService exampleService;
    @Autowired private SolvedService solvedService;

    @PostMapping
    public List<Problem> getAllProblems() {
        return problemService.getAllProblems();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Problem createProblem(@RequestBody ProblemDTO problem) {
        Problem curProblem = problemService.createProblem(problem.getProblem());

        for (Example example : problem.getExamples()) {
            example.setProblemId(curProblem.getId());
            exampleService.createExample(example);
        }

        return curProblem;
    }

    @PutMapping("/update")
    public ResponseEntity<Problem> updateProblem(@RequestBody ProblemDTO problem) {
        for (Example example : problem.getExamples()) {
            exampleService.updateExample(example);
        }
        return ResponseEntity.ok(problemService.updateProblem(problem.getProblem()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        exampleService.deleteExampleByProblemId(id);
        solvedService.deleteSolvedByProblemId(id);
        return ResponseEntity.noContent().build();
    }
}
