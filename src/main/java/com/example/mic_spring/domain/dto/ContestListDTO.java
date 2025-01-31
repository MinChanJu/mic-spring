package com.example.mic_spring.domain.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContestListDTO {
  private Long id;
  private Long contestId;
  private String contestName;
  private String contestDescription;
  private String userId;
  private ZonedDateTime startTime;
  private ZonedDateTime endTime;
}
