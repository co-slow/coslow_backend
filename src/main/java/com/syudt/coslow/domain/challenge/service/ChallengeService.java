package com.syudt.coslow.domain.challenge.service;

import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.repository.ChallengeRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Challenge createChallenge(ChallengeDTO challengeDTO) {
        Challenge challenge = new Challenge();
        challenge.setTitle(challengeDTO.getTitle());
        challenge.setDescription(challengeDTO.getDescription());
        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setEndDate(challengeDTO.getEndDate());
        challenge.setParticipateFrequency(challengeDTO.getParticipateFrequency());
        challenge.setMaxParticipants(challengeDTO.getMaxParticipants());
        challenge.setTags(challengeDTO.getTags());

        Member member = memberRepository.findById(Integer.parseInt(challengeDTO.getCreatedBy()))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        challenge.setCreatedBy(member.getNickname());  //Nickname으로 createdby 결정

        // user_role로 게시판 ID 결정
        int boardId;
        switch (member.getUserRole()) {
            case ADMIN:
                boardId = 1; //코슬로 챌린지 게시판
                break;
            case PTN:
                boardId = 2; //제휴 챌린지 게시판
                break;
            case USER:
            default:
                boardId = 3; //유저끼리 게시판
                break;
        }
        challenge.setBoardId(boardId);

        challenge.setCreateDate(LocalDateTime.now());
        challenge.setLastModifiedDate(LocalDateTime.now());

        challenge.updateStatus();

        return challengeRepository.save(challenge);
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        challenges.forEach(Challenge::updateStatus);
        return challenges;
    }
}
