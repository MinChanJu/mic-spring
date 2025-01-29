package com.example.mic_spring.service;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataService {

  private ContestService contestService;
  private ProblemService problemService;
  private SolveService solveService;
  private UserService userService;

  public DataService(ContestService contestService, ProblemService problemService, SolveService solveService,
      UserService userService) {
    this.contestService = contestService;
    this.problemService = problemService;
    this.solveService = solveService;
    this.userService = userService;
  }

  public List<ContestScoreDTO> getScoreBoardByContestId(Long contestId) {
    List<Problem> problems = problemService.getAllProblemsByContestId(contestId);
    List<UserDTO> participants = userService.getAllUserDTOsByContestId(contestId);

    List<ContestScoreDTO> contestScores = new ArrayList<>();
    for (UserDTO user : participants) {
      List<SubmitDTO> solveProblems = new ArrayList<>();
      for (Problem problem : problems) {
        Solve solve = solveService.existSolveByUserIdAndProblemId(user.getUserId(), problem.getId());
        if (solve == null)
          solveProblems.add(new SubmitDTO(problem.getId(), (short) 0));
        else
          solveProblems.add(new SubmitDTO(problem.getId(), solve.getScore()));
      }
      contestScores.add(new ContestScoreDTO(user.getName(), solveProblems));
    }
    contestScores.sort((a, b) -> {
      int sumA = 0, sumB = 0;
      for (SubmitDTO submit : a.getSolveProblems())
        sumA += submit.getScore();
      for (SubmitDTO submit : b.getSolveProblems())
        sumB += submit.getScore();
      return sumB - sumA;
    });
    return contestScores;
  }

  public ContestsAndProblemsDTO getAllFilterContestsAndProblems() {
    List<ContestListDTO> contests = contestService.getContestList();
    List<ProblemListDTO> problems = problemService.getProblemList();
    ContestsAndProblemsDTO contestsAndProblems = new ContestsAndProblemsDTO(contests, problems);
    return contestsAndProblems;
  }
}
