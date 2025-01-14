package com.example.my_gradle_spring_app.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.model.Problem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContestsAndProblemsDTO {
    private List<Contest> contests;
    private List<Problem> problems;
}
