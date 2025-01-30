package com.example.mic_spring.service;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.security.Token;

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

  public ScoreBoardDTO getScoreBoardByContestId(Long contestId, Token token) {
    ContestListDTO contest = contestService.getContestById(contestId);
    List<ProblemListDTO> problemList = problemService.getProblemListByContestId(contestId, token);
    List<UserDTO> participants = userService.getAllUserDTOsByContestId(contestId);

    List<ContestScoreDTO> contestScores = new ArrayList<>();
    for (UserDTO user : participants) {
      List<SubmitDTO> solveProblems = new ArrayList<>();
      for (ProblemListDTO problem : problemList) {
        Solve solve = solveService.existSolveByUserIdAndProblemId(user.getUserId(), problem.getProblemId());
        if (solve == null)
          solveProblems.add(new SubmitDTO(problem.getProblemId(), (short) 0));
        else
          solveProblems.add(new SubmitDTO(problem.getProblemId(), solve.getScore()));
      }
      contestScores.add(new ContestScoreDTO(-1L, user.getName(), solveProblems));
    }

    contestScores.sort((a, b) -> {
      int sumA = 0, sumB = 0;
      for (SubmitDTO submit : a.getSolveProblems())
        sumA += submit.getScore();
      for (SubmitDTO submit : b.getSolveProblems())
        sumB += submit.getScore();
      return sumB - sumA;
    });

    Long id = 1L;
    for (ContestScoreDTO contestScore : contestScores)
      contestScore.setId(id++);

    return new ScoreBoardDTO(contest, problemList, contestScores);
  }

  public ContestsAndProblemsDTO getAllFilterContestsAndProblems(Token token) {
    List<ContestListDTO> contests = contestService.getContestList();
    List<ProblemListDTO> problems = problemService.getProblemList(token);
    ContestsAndProblemsDTO contestsAndProblems = new ContestsAndProblemsDTO(contests, problems);
    return contestsAndProblems;
  }
}
