package com.example.my_gradle_spring_app.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.model.Problem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProblemDTO {
    private Problem problem;
    private List<Example> examples;
}
