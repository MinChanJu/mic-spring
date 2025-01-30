package com.example.mic_spring.service;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.exception.*;
import com.example.mic_spring.repository.*;
import com.example.mic_spring.security.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.ZonedDateTime;

@Service
public class ProblemService {

    private ProblemRepository problemRepository;
    private ContestService contestService;
    private ExampleService exampleService;
    private SolveService solveService;

    public ProblemService(ProblemRepository problemRepository, ContestService contestService,
            ExampleService exampleService, SolveService solveService) {
        this.problemRepository = problemRepository;
        this.contestService = contestService;
        this.exampleService = exampleService;
        this.solveService = solveService;
    }

    @Transactional(readOnly = true)
    public ProblemScoreDTO getProblemById(Long id, Token token) {
        if (id == null)
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> findProblem = problemRepository.findById(id);
        if (findProblem.isEmpty())
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Problem problem = findProblem.get();

        if (problem.getContestId() != null) {
            ContestListDTO contest = contestService.getContestById(problem.getContestId());
            if (!contest.getUserId().equals(token.getUserId()) && token.getAuthority() != 5) {
                if (contest.getStartTime() != null && contest.getStartTime().isAfter(ZonedDateTime.now())) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED);
                } else if (contest.getStartTime() != null && contest.getEndTime() != null
                        && contest.getEndTime().isAfter(ZonedDateTime.now())) {
                    if (contest.getId() != token.getContestId())
                        throw new CustomException(ErrorCode.UNAUTHORIZED);
                }
            }
        }

        Short score = -1;
        Solve solve = solveService.existSolveByUserIdAndProblemId(token.getUserId(), id);
        if (solve != null)
            score = solve.getScore();

        return new ProblemScoreDTO(problem, score);
    }

    @Transactional(readOnly = true)
    public List<Problem> getAllProblems() {
        return problemRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<ProblemListDTO> getProblemList(Token token) {
        List<Solve> solves = token.getUserId().equals("") ? new ArrayList<>()
                : solveService.getAllSolvesByUserIdOrderByProblemIdAsc(token.getUserId());
        List<Problem> problems = problemRepository.findAllByOrderByIdAsc();
        List<ProblemListDTO> problemList = new ArrayList<>();

        int idx = 0;
        int size = solves.size();
        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
            if (problem.getContestId() != null) {
                ContestListDTO contest = contestService.getContestById(problem.getContestId());
                if (contest.getStartTime() != null && contest.getStartTime().isAfter(ZonedDateTime.now()))
                    continue;
                if (contest.getEndTime() != null && contest.getEndTime().isAfter(ZonedDateTime.now()))
                    continue;
            }
            String problemName = problem.getProblemName();
            String contestName = contestService.getContestNameById(problem.getContestId());
            Short score = -1;
            if (idx < size && problemId == solves.get(idx).getProblemId()) {
                score = solves.get(idx).getScore();
                idx++;
            }
            problemList.add(new ProblemListDTO(id++, problemId, problemName, contestName, score));
        }

        return problemList;
    }

    @Transactional(readOnly = true)
    public Problem existsById(Long id) {
        if (id == null)
            return null;
        Optional<Problem> findProblem = problemRepository.findById(id);
        if (findProblem.isEmpty())
            return null;
        return findProblem.get();
    }

    @Transactional(readOnly = true)
    public List<ProblemListDTO> getProblemListByContestId(Long contestId, Token token) {
        ContestListDTO contest = contestService.getContestById(contestId);
        if (!contest.getUserId().equals(token.getUserId()) && token.getAuthority() != 5) {
            if (contest.getStartTime() != null && contest.getStartTime().isAfter(ZonedDateTime.now())) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            } else if (contest.getStartTime() != null && contest.getEndTime() != null
                    && contest.getEndTime().isAfter(ZonedDateTime.now())) {
                if (contestId != token.getContestId())
                    throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
        }

        List<Problem> problems = problemRepository.findByContestIdOrderByIdAsc(contestId);
        List<ProblemListDTO> problemList = new ArrayList<>();

        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
            String problemName = problem.getProblemName();
            String contestName = contestService.getContestNameById(problem.getContestId());
            Short score = -1;
            Solve solve = solveService.existSolveByUserIdAndProblemId(token.getUserId(), problemId);
            if (solve != null)
                score = solve.getScore();
            problemList.add(new ProblemListDTO(id++, problemId, problemName, contestName, score));
        }

        return problemList;
    }

    @Transactional(readOnly = true)
    public List<Problem> getAllProblemsByContestId(Long contestId) {
        return problemRepository.findByContestIdOrderByIdAsc(contestId);
    }

    @Transactional(readOnly = true)
    public List<Problem> getAllProblemsByUserId(String userId, Token token) {
        if (!token.getUserId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        return problemRepository.findByUserIdOrderByIdAsc(userId);
    }

    @Transactional(readOnly = true)
    public List<ProblemScoreDTO> getAllSolveProblemsByUserId(String userId, Token token) {
        if (!token.getUserId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        List<Solve> solves = solveService.getAllSolvesByUserId(userId);
        List<ProblemScoreDTO> problemScores = new ArrayList<>();
        for (Solve solve : solves) {
            Optional<Problem> findProblem = problemRepository.findById(solve.getProblemId());
            if (findProblem.isEmpty())
                continue;
            Problem problem = findProblem.get();
            Short score = solve.getScore();
            problemScores.add(new ProblemScoreDTO(problem, score));
        }
        return problemScores;
    }

    @Transactional
    public Problem createProblem(ProblemDTO problemDTO, Token token) {

        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (!problem.getUserId().equals(token.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        if (token.getAuthority() < 3)
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        if (problemRepository.existsByProblemName(problem.getProblemName()))
            throw new CustomException(ErrorCode.DUPLICATE_PROBLEM_NAME);

        Problem curProblem = problemRepository.save(problem);

        for (Example example : examples) {
            example.setProblemId(curProblem.getId());
            exampleService.createExample(example);
        }

        return curProblem;
    }

    @Transactional
    public Problem updateProblem(ProblemDTO problemDTO, Token token) {
        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (!problem.getUserId().equals(token.getUserId()) && token.getAuthority() != 5)
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        if (problem.getId() == null)
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        if (!problemRepository.existsById(problem.getId()))
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        for (Example example : examples) {
            if (example.getId() == null)
                exampleService.createExample(example);
            else
                exampleService.updateExample(example);
        }

        return problemRepository.save(problem);
    }

    @Transactional
    public void deleteProblem(Long id, Token token) {
        if (id == null)
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> findProblem = problemRepository.findById(id);
        if (findProblem.isEmpty())
            throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Problem problem = findProblem.get();

        if (!token.getUserId().equals(problem.getUserId()) && token.getAuthority() != 5)
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        problemRepository.delete(problem);
    }
}
