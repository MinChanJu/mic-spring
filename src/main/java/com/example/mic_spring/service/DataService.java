package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.ContestScoreDTO;
import com.example.mic_spring.domain.dto.ContestsAndProblemsDTO;
import com.example.mic_spring.domain.dto.SubmitDTO;
import com.example.mic_spring.domain.entity.Contest;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.domain.entity.Solve;
import com.example.mic_spring.domain.entity.User;

import java.util.List;
import java.time.ZonedDateTime;
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

    public ContestsAndProblemsDTO getAllFilterContestsAndProblems() {
        List<Contest> contests = contestService.getAllContests();
        List<Problem> problems = problemService.getAllProblems().stream()
        .filter(problem -> {
            if (problem.getContestId() == null) return true;
            Contest contest = contestService.getContestById(problem.getContestId());
            if (contest.getEndTime() == null) return true;
            if (contest.getEndTime().isBefore(ZonedDateTime.now())) return true;
            return false;
        }).toList();
        ContestsAndProblemsDTO contestsAndProblems = new ContestsAndProblemsDTO(contests, problems);
        return contestsAndProblems;
    }
}
