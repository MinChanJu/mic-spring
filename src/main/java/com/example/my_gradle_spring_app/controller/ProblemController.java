package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.model.ExampleDTO;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.ProblemDTO;
import com.example.my_gradle_spring_app.service.ExampleService;
import com.example.my_gradle_spring_app.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private ExampleService exampleService;

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Problem> getAllProblems() {
        return problemService.getAllProblems();
    }

    @PostMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Problem> getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Problem createProblem(@RequestBody ProblemDTO problemDTO) {
        try {
            // Problem 객체 생성 및 데이터 설정
            Problem problem = new Problem();
            problem.setContestId(problemDTO.getContestId());
            problem.setContestName(problemDTO.getContestName());
            problem.setUserId(problemDTO.getUserId());
            problem.setProblemName(problemDTO.getProblemName());
            problem.setProblemDescription(problemDTO.getProblemDescription());
            problem.setProblemInputDescription(problemDTO.getProblemInputDescription());
            problem.setProblemOutputDescription(problemDTO.getProblemOutputDescription());
            problem.setProblemExampleInput(problemDTO.getProblemExampleInput());
            problem.setProblemExampleOutput(problemDTO.getProblemExampleOutput());

            // Problem 저장
            Problem curProblem = problemService.createProblem(problem);

            // Example 저장
            for (ExampleDTO exampleDTO : problemDTO.getExamples()) {
                Example example = new Example();
                example.setExampleInput(exampleDTO.getExampleInput());
                example.setExampleOutput(exampleDTO.getExampleOutput());
                example.setProblemId(curProblem.getId().intValue());
                exampleService.createExample(example);
            }

            return curProblem;
        } catch (Exception e) {
            // 예외 발생 시 롤백 및 로그 출력
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the problem", e);
        }
    }

    @PostMapping("/examples/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Example> getProblemExamples(@PathVariable Long id) {
        return exampleService.getExamplesByProblemId(id.intValue());
    }

    @PutMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody ProblemDTO problemDetails) {
        return ResponseEntity.ok(problemService.updateProblem(id, problemDetails));
    }

    @DeleteMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}
