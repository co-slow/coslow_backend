package com.syudt.coslow.domain.challenge.controller;

import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.dto.SaveChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import com.syudt.coslow.domain.challenge.service.ChallengeService;
import com.syudt.coslow.domain.challenge.service.SaveChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private SaveChallengeService saveChallengeService;
    //새로운 챌린지 생성
    @PostMapping
    public ResponseEntity<?> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        try {
            Challenge createdChallenge = challengeService.createChallenge(challengeDTO);
            return ResponseEntity.ok(createdChallenge);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    //전체 챌린지 목록 조회 (챌린지 페이지)
    @GetMapping
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        List<Challenge> challenges = challengeService.getAllChallenges();
        challenges.forEach(Challenge::updateStatus);
        return ResponseEntity.ok(challenges);
    }
    //챌린지 저장
    @PostMapping("/save")
    public ResponseEntity<SaveChallenge> saveChallenge(@RequestBody SaveChallengeDTO saveChallengeDTO) {
        SaveChallenge savedChallenge = saveChallengeService.saveChallenge(saveChallengeDTO);
        return ResponseEntity.ok(savedChallenge);
    }
}
