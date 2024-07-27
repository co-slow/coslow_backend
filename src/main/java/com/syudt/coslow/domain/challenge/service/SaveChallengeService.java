package com.syudt.coslow.domain.challenge.service;

import com.syudt.coslow.domain.challenge.dto.SaveChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import com.syudt.coslow.domain.challenge.repository.ChallengeRepository;
import com.syudt.coslow.domain.challenge.repository.SaveChallengeRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class SaveChallengeService {

    @Autowired
    private SaveChallengeRepository saveChallengeRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public SaveChallenge saveChallenge(SaveChallengeDTO saveChallengeDTO) {
        //챌린지 id로 챌린지 정보 조회
        Challenge challenge = challengeRepository.findById(saveChallengeDTO.getChallengeId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 챌린지 ID입니다."));
        //사용자 id로 사용자 정보 조회
        Member member = memberRepository.findById(saveChallengeDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        SaveChallenge saveChallenge = new SaveChallenge();
        saveChallenge.setChallenge(challenge);
        saveChallenge.setMember(member);

        return saveChallengeRepository.save(saveChallenge);
    }
}
