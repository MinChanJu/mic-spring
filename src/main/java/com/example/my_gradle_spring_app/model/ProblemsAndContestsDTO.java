package com.example.my_gradle_spring_app.model;

import java.util.List;

public class ProblemsAndContestsDTO {
    private List<Problem> problems;
    private List<Contest> contests;

    public ProblemsAndContestsDTO(List<Problem> problems, List<Contest> contests) {
        this.problems = problems;
        this.contests = contests;
    }
    
    // Getters and setters
    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }
}
