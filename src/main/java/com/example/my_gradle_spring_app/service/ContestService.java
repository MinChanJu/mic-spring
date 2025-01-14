package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.repository.ContestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.OffsetDateTime;

@Service
public class ContestService {
    
    @Autowired private ContestRepository contestRepository;

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
        if (contestRepository.existsByContestName(contest.getContestName())) {
            Contest sub = new Contest();
            sub.setId(-1L);
            return sub;
        }
        return contestRepository.save(contest);
    }

    public Contest updateContest(Contest contest) {
        if (contestRepository.existsById(contest.getId())) return contestRepository.save(contest);

        Contest sub = new Contest();
        sub.setId(-1L);
        return sub;
    }

    public void deleteContest(Long id) {
        Contest contest = contestRepository.findById(id).orElseThrow(() -> new RuntimeException("Contest not found"));
        contestRepository.delete(contest);
    }
}
