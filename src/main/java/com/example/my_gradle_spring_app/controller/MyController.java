package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.ProblemsAndContestsDTO;
import com.example.my_gradle_spring_app.service.ContestService;
import com.example.my_gradle_spring_app.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class MyController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private ProblemService problemService;

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProblemsAndContestsDTO getProblemsAndContests() {
        List<Problem> problems = problemService.getAllProblems();
        List<Contest> contests = contestService.getAllContests();
        return new ProblemsAndContestsDTO(problems, contests);
    }
}
