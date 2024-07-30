package com.syudt.coslow.domain.challenge.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private SaveChallengeService saveChallengeService;

    // 새로운 챌린지 생성
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

    // 전체 챌린지 목록 조회 (챌린지 페이지)
    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges);
    }

    // 챌린지 저장
    @PostMapping("/save")
    public ResponseEntity<SaveChallenge> saveChallenge(@RequestBody SaveChallengeDTO saveChallengeDTO) {
        SaveChallenge savedChallenge = saveChallengeService.saveChallenge(saveChallengeDTO);
        return ResponseEntity.ok(savedChallenge);
    }

    // 마감순 챌린지 목록 조회 (현재 날짜 기준 종료일이 가까운 순으로, 종료된 챌린지는 그 뒤에 정렬)
    @GetMapping("/ending-soon")
    public ResponseEntity<List<ChallengeDTO>> getChallengesEndingSoon() {
        List<ChallengeDTO> challenges = challengeService.getChallengesEndingSoon();
        return ResponseEntity.ok(challenges);
    }

    // 인기순 챌린지 목록 조회 (저장 많이한 순)
    @GetMapping("/popular")
    public ResponseEntity<List<ChallengeDTO>> getPopularChallenges() {
        List<ChallengeDTO> challenges = challengeService.getPopularChallenges();
        return ResponseEntity.ok(challenges);
    }

    // 최신순 챌린지 목록 조회 (최근에 올린 챌린지)
    @GetMapping("/newest")
    public ResponseEntity<List<ChallengeDTO>> getNewestChallenges() {
        List<ChallengeDTO> challenges = challengeService.getNewestChallenges();
        return ResponseEntity.ok(challenges);
    }
}
