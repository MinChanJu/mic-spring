package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.ContestScore;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.ProblemsAndContestsDTO;
import com.example.my_gradle_spring_app.model.Solved;
import com.example.my_gradle_spring_app.model.Submit;
import com.example.my_gradle_spring_app.model.User;
import com.example.my_gradle_spring_app.service.ContestService;
import com.example.my_gradle_spring_app.service.ProblemService;
import com.example.my_gradle_spring_app.service.SolvedService;
import com.example.my_gradle_spring_app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class MyController {

    @Autowired private ContestService contestService;
    @Autowired private ProblemService problemService;
    @Autowired private UserService userService;
    @Autowired private SolvedService solvedService;

    @PostMapping
    public ProblemsAndContestsDTO getProblemsAndContests() {
        List<Problem> problems = problemService.getAllProblems();
        List<Contest> contests = contestService.getAllContests();
        return new ProblemsAndContestsDTO(problems, contests);
    }

    @PostMapping("/{contestId}")
    public List<ContestScore> getContestScore(@PathVariable Long contestId) {
        List<Problem> problems = problemService.getProblemByContestId(contestId);
        List<User> participants = userService.getUserByContest(contestId);

        List<ContestScore> contestScores = new ArrayList<>();
        for (User user : participants) {
            List<Submit> solveProblems = new ArrayList<>();
            for (Problem problem : problems) {
                Solved solved = solvedService.getSolvedByUserIdAndProblemId(user.getUserId(), problem.getId());
                if (solved == null) solveProblems.add(new Submit(problem.getId(), "0"));
                else solveProblems.add(new Submit(problem.getId(), solved.getScore()));
            }
            contestScores.add(new ContestScore(user.getName(), solveProblems));
        }

        return contestScores;
    }
}
