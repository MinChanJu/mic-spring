package com.example.my_gradle_spring_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.my_gradle_spring_app.dto.ContestsAndProblemsDTO;
import com.example.my_gradle_spring_app.dto.ContestScoreDTO;
import com.example.my_gradle_spring_app.dto.SubmitDTO;
import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.model.Solve;
import com.example.my_gradle_spring_app.model.User;

import java.util.List;
import java.util.ArrayList;

@Service
public class DataService {

    @Autowired private ContestService contestService;
    @Autowired private ProblemService problemService;
    @Autowired private SolveService solveService;
    @Autowired private UserService userService;

    public ContestsAndProblemsDTO getAllContestsAndProblems() {
        List<Contest> contests = contestService.getAllContests();
        List<Problem> problems = problemService.getAllProblems();
        ContestsAndProblemsDTO contestsAndProblems = new ContestsAndProblemsDTO(contests, problems);
        return contestsAndProblems;
    }

    public List<ContestScoreDTO> getScoreBoardByContestId(Long contestId) {
        List<Problem> problems = problemService.getAllProblemsByContestId(contestId);
        List<User> participants = userService.getAllUsersByContestId(contestId);

        List<ContestScoreDTO> contestScores = new ArrayList<>();
        for (User user : participants) {
            List<SubmitDTO> solveProblems = new ArrayList<>();
            for (Problem problem : problems) {
                Solve solved = solveService.getSolveByUserIdAndProblemId(user.getUserId(), problem.getId());
                if (solved == null) solveProblems.add(new SubmitDTO(problem.getId(), (short) 0));
                else solveProblems.add(new SubmitDTO(problem.getId(), solved.getScore()));
            }
            contestScores.add(new ContestScoreDTO(user.getName(), solveProblems));
        }

        return contestScores;
    }
}
