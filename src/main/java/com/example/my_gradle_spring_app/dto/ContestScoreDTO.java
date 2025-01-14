package com.example.my_gradle_spring_app.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContestScoreDTO {
    private String name;
    private List<SubmitDTO> solvedProblems;
}
