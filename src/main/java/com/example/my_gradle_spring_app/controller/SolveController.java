package com.example.my_gradle_spring_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_gradle_spring_app.model.Solve;
import com.example.my_gradle_spring_app.service.SolveService;

@RestController
@RequestMapping("/api/solves")
public class SolveController {
    
    @Autowired private SolveService solvedService;

    @PostMapping
    public Solve solvedProblem(@RequestBody Solve solved) {
        Solve findSolved = solvedService.getSolvedByUserIdAndProblemId(solved.getUserId(), solved.getProblemId());

        if (findSolved != null) {
            if (findSolved.getScore() > solved.getScore()) return findSolved;
            solved.setId(findSolved.getId());
            return solvedService.updateSolved(solved);
        }
        return solvedService.createSolved(solved);
    }
    
    @PostMapping("/{userId}")
    public List<Solve> getSolvedOfUser(@PathVariable String userId) {
        return solvedService.getSolvedsByUserId(userId);
    }

    @PostMapping("/problem/{problemId}")
    public List<Solve> getSolvedOfUser(@PathVariable Long problemId) {
        return solvedService.getSolvedsByProblemId(problemId);
    }
}
