package com.syudt.coslow.domain.challenge.controller;

import com.syudt.coslow.auth.service.AuthService;
import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.service.ApplyChallengeService;
import com.syudt.coslow.domain.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final AuthService authService;

    private final ApplyChallengeService applyChallengeService;

    private final ChallengeService challengeService;

    // 새로운 챌린지 생성
    @PostMapping
    public ResponseEntity<Challenge> createChallenge(@RequestBody ChallengeDTO challengeDTO) {
        try {
            Challenge createdChallenge = challengeService.createChallenge(challengeDTO);
            return ResponseEntity.ok(createdChallenge);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 전체 챌린지 목록 조회 (챌린지 페이지)
    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges);
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

    // 챌린지 신청
    @PostMapping("/{challengeId}/apply")
    public ResponseEntity<String> applyToChallenge(@PathVariable int challengeId, @RequestParam int userId) {
        String response = applyChallengeService.applyToChallenge(challengeId, userId);
        if (response.startsWith("챌린지 신청이 성공적으로")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }


    // 참여 인원 수 조회

    @GetMapping("/{challengeId}/participants/count")
    public ResponseEntity<Integer> getParticipantsCount(@PathVariable int challengeId) {
        int count = applyChallengeService.getParticipantsCount(challengeId);
        return ResponseEntity.ok(count);
    }

    // 사용자가 참여한 챌린지 조회 (게시판 및 상태별)
    //http://localhost:8080/challenges/user/1/board/3/status/RECRUITING
    @GetMapping("/user/{boardId}")
    public ResponseEntity<List<?>> getUserChallengesByBoardAndStatus(@PathVariable("boardId") Integer boardId, RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];
        String oauthId = authService.isTokenValid(accessToken);

        try {
            List<?> challenges = applyChallengeService.getChallengesForUserByBoard(oauthId, boardId);
            return ResponseEntity.ok(challenges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}

