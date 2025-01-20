package com.example.mic_spring.domain.dto;

import java.util.List;

import com.example.mic_spring.domain.entity.Solve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CodeResultDTO {
    private String result;
    private List<Solve> solves;
}
