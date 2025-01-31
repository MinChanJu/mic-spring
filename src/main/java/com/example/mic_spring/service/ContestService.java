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
public class ContestService {

  private ContestRepository contestRepository;

  public ContestService(ContestRepository contestRepository) {
    this.contestRepository = contestRepository;
  }

  @Transactional(readOnly = true)
  public List<ContestListDTO> getContestList() {
    List<Contest> contests = contestRepository.findAllOrderByStartTimeDescOrCreatedAtDesc();
    List<ContestListDTO> contestList = new ArrayList<>();

    Long idx = 1L;
    for (Contest contest : contests) {
      Long id = idx++;
      Long contestId = contest.getId();
      String contestName = contest.getContestName();
      String contestDescription = contest.getContestDescription();
      String userId = contest.getUserId();
      ZonedDateTime startTime = contest.getStartTime();
      ZonedDateTime endTime = contest.getEndTime();
      contestList.add(
          new ContestListDTO(id, contestId, contestName, contestDescription, userId, startTime, endTime));
    }

    return contestList;
  }

  @Transactional(readOnly = true)
  public List<ContestListDTO> getContestListForUserId(Token token) {
    List<Contest> contests = contestRepository.findByUserIdOrderByIdAsc(token.getUserId());
    List<ContestListDTO> contestList = new ArrayList<>();

    Long idx = 1L;
    for (Contest contest : contests) {
      Long id = idx++;
      Long contestId = contest.getId();
      String contestName = contest.getContestName();
      String contestDescription = contest.getContestDescription();
      String userId = contest.getUserId();
      ZonedDateTime startTime = contest.getStartTime();
      ZonedDateTime endTime = contest.getEndTime();
      contestList.add(
          new ContestListDTO(id, contestId, contestName, contestDescription, userId, startTime, endTime));
    }
    return contestList;
  }

  @Transactional(readOnly = true)
  public ContestListDTO getContestById(Long id) {
    if (id == null)
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
    Optional<Contest> findContest = contestRepository.findById(id);
    if (findContest.isEmpty())
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);

    Contest contest = findContest.get();

    Long idx = 1L;
    Long contestId = contest.getId();
    String contestName = contest.getContestName();
    String contestDescription = contest.getContestDescription();
    String userId = contest.getUserId();
    ZonedDateTime startTime = contest.getStartTime();
    ZonedDateTime endTime = contest.getEndTime();

    return new ContestListDTO(idx, contestId, contestName, contestDescription, userId, startTime, endTime);
  }

  @Transactional(readOnly = true)
  public String getContestNameById(Long id) {
    if (id == null)
      return "이 문제는 대회에 속하지 않습니다.";
    Optional<Contest> contest = contestRepository.findById(id);
    if (contest.isEmpty())
      return "이 문제는 대회에 속하지 않습니다.";
    return contest.get().getContestName();
  }

  @Transactional
  public Contest createContest(Contest contest, Token token) {
    if (contestRepository.existsByContestName(contest.getContestName()))
      throw new CustomException(ErrorCode.DUPLICATE_CONTEST_NAME);
    if (!contest.getUserId().equals(token.getUserId()))
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    if (token.getAuthority() < 3)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    return contestRepository.save(contest);
  }

  @Transactional
  public Contest updateContest(Contest contest, Token token) {
    if (contest.getId() == null)
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
    Optional<Contest> findContest = contestRepository.findById(contest.getId());
    if (findContest.isEmpty())
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
    Contest prev = findContest.get();

    if (!token.getUserId().equals(prev.getUserId()) && token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    return contestRepository.save(contest);
  }

  @Transactional
  public void deleteContest(Long id, Token token) {
    if (id == null)
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);
    Optional<Contest> contest = contestRepository.findById(id);
    if (contest.isEmpty())
      throw new CustomException(ErrorCode.CONTEST_NOT_FOUND);

    if (!token.getUserId().equals(contest.get().getUserId()) && token.getAuthority() != 5)
      throw new CustomException(ErrorCode.UNAUTHORIZED);

    contestRepository.delete(contest.get());
  }
}
