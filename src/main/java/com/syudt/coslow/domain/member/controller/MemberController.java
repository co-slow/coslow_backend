package com.syudt.coslow.domain.member.controller;

import com.syudt.coslow.auth.service.AuthService;
import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.member.dto.MemberSave;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getMyPage(RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];
        String oauthId = authService.isTokenValid(accessToken);
        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        Integer challengeCount;
        String charLevel;
        Integer charPoint = 0;
        String grade;
        if (member.getChallengeCount().toString().equals("null")) {
            challengeCount = 0;
        } else {
            challengeCount = member.getChallengeCount();
        }

        if (challengeCount >= 0  && challengeCount < 10) {
            charLevel = "1";
        } else if (challengeCount >= 10  && challengeCount < 30) {
            charPoint = challengeCount - 10;
            charLevel = "2";
        }  else if (challengeCount >= 30  && challengeCount < 50) {
            charLevel = "3";
        }  else if (challengeCount >= 50  && challengeCount < 100) {
            charLevel = "4";
        }  else {
            charLevel = "5";
        }
        grade = getGrade(challengeCount);

        MemberSave memberSave = MemberSave.builder()
                .oauthId(oauthId)
                .profileImg(member.getProfileImg())
                .nickname(member.getNickname())
                .userRole(member.getUserRole())
                .grade(grade)
                .charPoint(charPoint)
                .challengeCount(challengeCount)
                .charLevel(charLevel)
                .build();

        return ResponseEntity.ok(memberSave);₩
    }

    @GetMapping("/saved")
    public ResponseEntity<List<ChallengeDTO>> getSavedChallenges(@RequestParam("oauthId") String oauthId) {
        List<Challenge> savedChallenges = memberService.getSavedChallengesByOauthId(oauthId);
        List<ChallengeDTO> savedChallengeDTOs = savedChallenges.stream()
                .map(ChallengeDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(savedChallengeDTOs);
    }

    private String getGrade(int challengeCount) {
        if (challengeCount < 10) {
            return "아기코북";
        } else if (challengeCount < 30) {
            return "멋진 코북";
        } else if (challengeCount < 50) {
            return "똑똑한 코북";
        } else if (challengeCount < 100) {
            return "용감한 코북";
        } else {
            return "전설의 코북";
        }
    }
}
