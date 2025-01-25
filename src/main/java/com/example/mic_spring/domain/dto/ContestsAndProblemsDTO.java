package com.example.mic_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.example.mic_spring.domain.entity.Contest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContestsAndProblemsDTO {
    private List<Contest> contests;
    private List<ProblemListDTO> problems;
}
