package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.repository.ContestRepository;
import com.example.my_gradle_spring_app.repository.ProblemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.OffsetDateTime;

@Service
public class ContestService {
    
    @Autowired private ContestRepository contestRepository;
    @Autowired private ProblemRepository problemRepository;
    @Autowired private ProblemService problemService;

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public List<Contest> getBeforeContests() {
        return contestRepository.findByEventTimeBefore(OffsetDateTime.now());
    }

    public List<Contest> getAfterContests() {
        return contestRepository.findByEventTimeAfter(OffsetDateTime.now());
    }

    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }

    public Contest updateContest(Long id, Contest contestDetails) {
        Contest contest = contestRepository.findById(id).orElseThrow(() -> new RuntimeException("Contest not found"));

        contest.setContestName(contestDetails.getContestName());
        contest.setContestDescription(contestDetails.getContestDescription());
        contest.setContestPw(contestDetails.getContestPw());

        return contestRepository.save(contest);
    }

    public void deleteContest(Long id) {
        Contest contest = contestRepository.findById(id).orElseThrow(() -> new RuntimeException("Contest not found"));
        contestRepository.delete(contest);
        List<Problem> problems = problemRepository.findByContestId(id.intValue());
        for (Problem problem : problems) {
            problemService.deleteProblem(problem.getId());
        }
    }
}
