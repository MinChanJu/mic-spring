package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.repository.ContestRepository;
import com.example.my_gradle_spring_app.repository.ProblemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContestService {
    
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemService problemService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Contest updateContest(Long id, Contest contestDetails) {
        Contest contest = contestRepository.findById(id).orElseThrow(() -> new RuntimeException("Contest not found"));

        contest.setContestName(contestDetails.getContestName());
        contest.setContestDescription(contestDetails.getContestDescription());
        contest.setContestPw(contestDetails.getContestPw());

        return contestRepository.save(contest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteContest(Long id) {
        Contest contest = contestRepository.findById(id).orElseThrow(() -> new RuntimeException("Contest not found"));
        contestRepository.delete(contest);
        List<Problem> problems = problemRepository.findByContestId(id.intValue());
        for (Problem problem : problems) {
            problemService.deleteProblem(problem.getId());
        }
    }
}
