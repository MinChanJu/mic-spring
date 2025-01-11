package com.example.my_gradle_spring_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_gradle_spring_app.model.Solved;
import com.example.my_gradle_spring_app.service.SolvedService;

@RestController
@RequestMapping("/api/solveds")
public class SolvedController {
    
    @Autowired private SolvedService solvedService;

    @PostMapping
    public Solved solvedProblem(@RequestBody Solved solved) {
        Solved findSolved = solvedService.getSolvedByUserIdAndProblemId(solved.getUserId(), solved.getProblemId());

        if (findSolved != null) {
            if (Float.parseFloat(findSolved.getScore()) > Float.parseFloat(solved.getScore())) return findSolved;
            solved.setId(findSolved.getId());
            return solvedService.updateSolved(solved);
        }
        return solvedService.createSolved(solved);
    }
    
    @PostMapping("/{userId}")
    public List<Solved> getSolvedOfUser(@PathVariable String userId) {
        return solvedService.getSolvedsByUserId(userId);
    }

    @PostMapping("/problem/{problemId}")
    public List<Solved> getSolvedOfUser(@PathVariable Long problemId) {
        return solvedService.getSolvedsByProblemId(problemId);
    }
}
