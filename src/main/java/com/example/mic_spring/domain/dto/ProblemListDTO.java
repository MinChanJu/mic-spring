package com.example.mic_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProblemListDTO {
    private Long id;
    private Long problemId;
    private String problemName;
    private String contestName;
    private Short score;
}
