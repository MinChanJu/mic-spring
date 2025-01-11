package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.service.ContestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired private ContestService contestService;

    @PostMapping
    public List<Contest> getAllContests() {
        return contestService.getAllContests();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Contest> getContestById(@PathVariable Long id) {
        return contestService.getContestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Contest createContest(@RequestBody Contest contest) {
        return contestService.createContest(contest);
    }

    @PutMapping("/update")
    public ResponseEntity<Contest> updateContest(@RequestBody Contest contest) {
        return ResponseEntity.ok(contestService.updateContest(contest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return ResponseEntity.noContent().build();
    }
}