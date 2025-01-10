package com.example.my_gradle_spring_app.model;

import java.util.List;

public class ContestScore {
    private String name;
    private List<Submit> solvedProblems;

    public ContestScore(String name, List<Submit> solvedProblems) { 
        this.name = name;
        this.solvedProblems = solvedProblems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Submit> getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(List<Submit> solvedProblems) {
        this.solvedProblems = solvedProblems;
    }
}
