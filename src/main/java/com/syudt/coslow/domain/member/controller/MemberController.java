package com.syudt.coslow.domain.member.controller;

import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.member.dto.MemberSave;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getMyPage(@RequestParam("oauthId") String oauthId) {
        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        int challengeCount = member.getChallengeCount() != null ? member.getChallengeCount() : 0;
        int charPoint = member.getCharPoint() != null ? member.getCharPoint() : 0;
        String charLevel = member.getCharLevel() != null ? member.getCharLevel() : "1";
        String grade = getGrade(challengeCount);

        MemberSave mypageResponse = MemberSave.builder()
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .charLevel(charLevel)
                .userRole(member.getUserRole())
                .grade(grade)
                .charPoint(charPoint)
                .challengeCount(challengeCount)
                .build();

        return ResponseEntity.ok(mypageResponse);
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
