package com.example.my_gradle_spring_app.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CodeDTO {
    private String code;
    private String lang;
    private Long problemId;
}
