package com.syudt.coslow.domain.challenge.controller;

import com.amazonaws.Request;
import com.syudt.coslow.auth.service.AuthService;
import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.dto.SaveChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.ApplyChallenge;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import com.syudt.coslow.domain.challenge.repository.ApplyChallengeRepository;
import com.syudt.coslow.domain.challenge.repository.ChallengeRepository;
import com.syudt.coslow.domain.challenge.repository.SaveChallengeRepository;
import com.syudt.coslow.domain.challenge.service.ApplyChallengeService;
import com.syudt.coslow.domain.challenge.service.ChallengeService;
import com.syudt.coslow.domain.challenge.service.SaveChallengeService;
import com.syudt.coslow.domain.diet.repository.DietRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final AuthService authService;

    private final ApplyChallengeService applyChallengeService;

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;
    private final SaveChallengeService saveChallengeService;
    private final MemberRepository memberRepository;
    private final SaveChallengeRepository saveChallengeRepository;
//    private final DietRepository dietRepository;
    private final ApplyChallengeRepository applyChallengeRepository;
    // 새로운 챌린지 생성
    @PostMapping
    public ResponseEntity<Challenge> createChallenge(@RequestBody ChallengeDTO challengeDTO, RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];
        String oauthId = authService.isTokenValid(accessToken);


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
    @PostMapping("/save")
    public ResponseEntity<SaveChallenge> saveChallenge(@RequestBody SaveChallengeDTO saveChallengeDTO) {
        SaveChallenge savedChallenge = saveChallengeService.saveChallenge(saveChallengeDTO);
        return ResponseEntity.ok(savedChallenge);
    }

    @GetMapping("/saved")
    public ResponseEntity<List<SaveChallenge>> getSavedChallengeViaToken(RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];
        String oauthId = authService.isTokenValid(accessToken);

        Member member = memberRepository.findByOauthId(oauthId).orElseThrow();
        List<SaveChallenge> saveChallenges = saveChallengeRepository.findByMember(member);

        return ResponseEntity.ok().body(saveChallenges);
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
    public ResponseEntity<String> applyToChallenge(@PathVariable int challengeId, RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];
        String oauthId = authService.isTokenValid(accessToken);

        String response = applyChallengeService.applyToChallenge(challengeId, oauthId);
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

    @GetMapping("/{challengeId}/detail")
    public ResponseEntity<MultiValueMap<Object, Object>> getChallengeDetail(@PathVariable("challengeId") Integer challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow();
        MultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.add("id", challenge.getChallengeId());
        body.add("title", challenge.getTitle());
        body.add("startDate", challenge.getStartDate());
        body.add("endDate", challenge.getEndDate());

        List<ApplyChallenge> applyList = applyChallengeRepository.findByChallenge(challenge);
        System.out.println(applyList);

        return ResponseEntity.ok().body(body);

    }

    @GetMapping("/{challengeId}/dday")
    public String getChallengeDday(@PathVariable("challengeId") Integer challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow();

        String daysRemaining = "0";
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = java.time.Duration.between(now, challenge.getEndDate()).toDays();
        daysBetween += 1;
        if (daysBetween > 0) {
            daysRemaining = "D-" + daysBetween;
        } else if (daysBetween == 0) {
            daysRemaining = "D-DAY";
        } else {
            daysRemaining = "종료";
        }
        return daysRemaining;
    }
}

