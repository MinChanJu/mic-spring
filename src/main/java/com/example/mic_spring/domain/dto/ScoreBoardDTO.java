package com.example.mic_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScoreBoardDTO {
  private ContestListDTO contest;
  private List<ProblemListDTO> problemList;
  private List<ContestScoreDTO> contestScores;
}
