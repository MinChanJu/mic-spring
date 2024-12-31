package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.Contest;
import com.example.my_gradle_spring_app.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Contest> getAllContests() {
        return contestService.getAllContests();
    }

    @PostMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Contest> getContestById(@PathVariable Long id) {
        return contestService.getContestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Contest createContest(@RequestBody Contest contest) {
        return contestService.createContest(contest);
    }

    @PutMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Contest> updateContest(@PathVariable Long id, @RequestBody Contest contestDetails) {
        return ResponseEntity.ok(contestService.updateContest(id, contestDetails));
    }

    @DeleteMapping("/{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return ResponseEntity.noContent().build();
    }
}