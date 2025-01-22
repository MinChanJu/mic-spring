package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.ProblemDTO;
import com.example.mic_spring.domain.dto.ProblemListDTO;
import com.example.mic_spring.domain.dto.ProblemScoreDTO;
import com.example.mic_spring.domain.entity.Example;
import com.example.mic_spring.domain.entity.Problem;
import com.example.mic_spring.domain.entity.Solve;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.ProblemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired private ProblemRepository problemRepository;
    @Autowired private ContestService contestService;
    @Autowired private ExampleService exampleService;
    @Autowired private SolveService solveService;

    public Problem getProblemById(Long id) {
        if (id == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        return problem.get();
    }

    public List<Problem> getAllProblems() {
        return problemRepository.findAllByOrderByIdAsc();
    }

    public List<ProblemListDTO> getProblemList() {
        List<Problem> problems = problemRepository.findAllByOrderByIdAsc();
        List<ProblemListDTO> problemList = new ArrayList<>();

        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
            String problemName = problem.getProblemName();
            String contestName = contestService.getContestNameById(problem.getContestId());
            Short score = -1;
            problemList.add(new ProblemListDTO(id++, problemId, problemName, contestName, score));
        }

        return problemList;
    }

    public List<ProblemListDTO> getProblemListWithUserId(String userId) {
        List<Solve> solves = solveService.getAllSolvesByUserIdOrderByProblemIdAsc(userId);
        List<Problem> problems = problemRepository.findAllByOrderByIdAsc();
        List<ProblemListDTO> problemList = new ArrayList<>();

        int idx = 0;
        int size = solves.size();
        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
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

    public List<ProblemListDTO> getProblemListByContestId(Long contestId) {
        List<Problem> problems = problemRepository.findByContestIdOrderByIdAsc(contestId);
        List<ProblemListDTO> problemList = new ArrayList<>();

        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
            String problemName = problem.getProblemName();
            String contestName = contestService.getContestNameById(problem.getContestId());
            Short score = -1;
            problemList.add(new ProblemListDTO(id++, problemId, problemName, contestName, score));
        }

        return problemList;
    }

    public List<ProblemListDTO> getProblemListByContestIdWithUserId(Long contestId, String userId) {
        List<Problem> problems = problemRepository.findByContestIdOrderByIdAsc(contestId);
        List<ProblemListDTO> problemList = new ArrayList<>();

        Long id = 1L;

        for (Problem problem : problems) {
            Long problemId = problem.getId();
            String problemName = problem.getProblemName();
            String contestName = contestService.getContestNameById(problem.getContestId());
            Short score = -1;
            Solve solve = solveService.existSolveByUserIdAndProblemId(userId, problemId);
            if (solve != null) score = solve.getScore();
            problemList.add(new ProblemListDTO(id++, problemId, problemName, contestName, score));
        }

        return problemList;
    }

    public List<Problem> getAllProblemsByContestId(Long contestId) {
        return problemRepository.findByContestIdOrderByIdAsc(contestId);
    }

    public List<Problem> getAllProblemsByUserId(String userId) {
        return problemRepository.findByUserIdOrderByIdAsc(userId);
    }

    public List<ProblemScoreDTO> getAllSolveProblemsByUserId(String userId) {
        List<Solve> solves = solveService.getAllSolvesByUserId(userId);
        List<ProblemScoreDTO> problemScores = new ArrayList<>();
        for (Solve solve : solves) {
            Problem problem = getProblemById(solve.getProblemId());
            Long problemId = problem.getId();
            String problemName = problem.getProblemName();
            Short score = solve.getScore();
            problemScores.add(new ProblemScoreDTO(problemId, problemName, score));
        }
        return problemScores;
    }

    public Problem createProblem(ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (problemRepository.existsByProblemName(problem.getProblemName())) throw new CustomException(ErrorCode.DUPLICATE_PROBLEM_NAME);

        Problem curProblem = problemRepository.save(problem);

        for (Example example : examples) {
            example.setProblemId(curProblem.getId());
            exampleService.createExample(example);
        }

        return curProblem;
    }

    public Problem updateProblem(ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<Example> examples = problemDTO.getExamples();

        if (problem.getId() == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        if (!problemRepository.existsById(problem.getId())) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        for (Example example : examples) {
            if (example.getId() == null) exampleService.createExample(example);
            else exampleService.updateExample(example);
        }
        
        return problemRepository.save(problem);
    }

    public void deleteProblem(Long id) {
        if (id == null) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);

        problemRepository.delete(problem.get());
    }
}
