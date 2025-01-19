package com.example.mic_spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.entity.Contest;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;
import com.example.mic_spring.repository.ContestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContestService {
    
    @Autowired private ContestRepository contestRepository;

    public Contest getContestById(Long id) {
        if (id == null) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
        Optional<Contest> contest = contestRepository.findById(id);
        if (contest.isEmpty()) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);

        return contest.get();
    }

    public List<Contest> getAllContestsByUserId(String userId) {
        return contestRepository.findByUserId(userId);
    }

    public List<Contest> getAllContests() {
        List<Contest> contests = contestRepository.findAll();
        contests.sort((a, b) -> {
            if (a.getStartTime() == null && b.getStartTime() == null) return a.getCreatedAt().isBefore(b.getCreatedAt()) ? 1 : -1;
            else if (a.getStartTime() == null) return 1;
            else if (b.getStartTime() == null) return -1;
            return a.getStartTime().isBefore(b.getStartTime()) ? 1 : -1;
        });
        return contests;
    }

    public Contest createContest(Contest contest) {
        if (contestRepository.existsByContestName(contest.getContestName()))  throw new CustomException(ErrorCode.DUPLICATE_CONTEST_NAME);

        return contestRepository.save(contest);
    }

    public Contest updateContest(Contest contest) {
        if (contest.getId() == null) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
        if (!contestRepository.existsById(contest.getId())) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);

        return contestRepository.save(contest);
    }

    public void deleteContest(Long id) {
        if (id == null) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
        Optional<Contest> contest = contestRepository.findById(id);
        if (contest.isEmpty()) throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);

        contestRepository.delete(contest.get());
    }
}
