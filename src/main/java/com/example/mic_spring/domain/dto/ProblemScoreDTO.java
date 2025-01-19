package com.example.mic_spring.domain.dto;

import com.example.mic_spring.domain.entity.Problem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProblemScoreDTO {
    private Problem problem;
    private short score;
}
