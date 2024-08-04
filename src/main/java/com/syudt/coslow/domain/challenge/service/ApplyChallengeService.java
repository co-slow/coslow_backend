package com.syudt.coslow.domain.challenge.service;

import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.ApplyChallenge;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.repository.ApplyChallengeRepository;
import com.syudt.coslow.domain.challenge.repository.ChallengeRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ApplyChallengeService {

    @Autowired
    private ApplyChallengeRepository applyChallengeRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 챌린지 신청
    public String applyToChallenge(int challengeId, int userId) {
        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);
        if (challengeOptional.isEmpty()) {
            return "챌린지를 찾을 수 없습니다.";
        }
        Challenge challenge = challengeOptional.get();

        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (memberOptional.isEmpty()) {
            return "사용자를 찾을 수 없습니다.";
        }
        Member member = memberOptional.get();

        if (challenge.getStatus() == Challenge.ChallengeStatus.COMPLETED) {
            return "이미 종료된 챌린지입니다.";
        }

        long currentParticipants = applyChallengeRepository.countByChallenge(challenge);
        if (currentParticipants >= challenge.getMaxParticipants()) {
            return "챌린지의 최대 참가 인원이 초과되었습니다.";
        }

        if (applyChallengeRepository.existsByChallengeAndMember(challenge, member)) {
            return "이미 신청한 챌린지입니다.";
        }

        ApplyChallenge applyChallenge = new ApplyChallenge();
        applyChallenge.setChallenge(challenge);
        applyChallenge.setMember(member);
        applyChallenge.setApplyDate(LocalDateTime.now());

        if (challenge.getStartDate().isAfter(LocalDateTime.now())) {
            applyChallenge.setStatus(ApplyChallenge.Status.PENDING);
            applyChallengeRepository.save(applyChallenge);
            return "챌린지가 시작하기 전입니다. 챌린지가 시작되면 자동으로 참여가 승인됩니다.";
        } else {
            applyChallenge.setStatus(ApplyChallenge.Status.APPROVED);
            applyChallengeRepository.save(applyChallenge);
            long numberOfParticipants = applyChallengeRepository.countByChallenge(challenge);
            return "챌린지 신청이 성공적으로 완료되었습니다. 현재 참가 인원: " + numberOfParticipants + "명";
        }
    }

    // 사용자가 신청한 챌린지 목록 조회
    public List<ChallengeDTO> getChallengesForUser(int userId) {
        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        Member member = memberOptional.get();

        List<ApplyChallenge> applications = applyChallengeRepository.findByMember(member);
        return applications.stream()
                .map(applyChallenge -> ChallengeDTO.fromEntity(applyChallenge.getChallenge()))
                .collect(Collectors.toList());
    }

    // 특정 챌린지의 참가자 수 조회
    public int getParticipantsCount(int challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));
        return (int) applyChallengeRepository.countByChallenge(challenge);
    }

    // 사용자가 특정 게시판의 챌린지 상태별로 조회
    public List<? extends Object> getChallengesForUserByBoard(String oauthId, Integer boardId) {
        System.out.println("UserID: " + oauthId+ ", BoardID: " + boardId);

        Member memberOptional = memberRepository.findByOauthId(oauthId).orElseThrow();

        List<ApplyChallenge> applications = applyChallengeRepository.findByMember(memberOptional);
        System.out.println("Applications: " + applications);

        if (boardId == 4) {
            return challengeRepository.findByCreatedBy(memberOptional.getNickname());
        } else {
            return applications.stream()
                    .map(applyChallenge -> applyChallenge.getChallenge())
                    .filter(challenge -> challenge.getBoardId() == boardId)
                    .map(challenge -> ChallengeDTO.fromEntity(challenge))
                    .collect(Collectors.toList());    
        }

        
    }
}
