package com.syudt.coslow.domain.challenge.service;

import com.syudt.coslow.domain.challenge.dto.ChallengeDTO;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.repository.ChallengeRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//참여자 최대 nn명 까지 가능
    public Challenge createChallenge(ChallengeDTO challengeDTO) {
        if (challengeDTO.getMaxParticipants() > 99) {
            throw new IllegalArgumentException("참여자는 최대 99명까지 가능합니다.");
        }

        Challenge challenge = new Challenge();
        challenge.setTitle(challengeDTO.getTitle());
        challenge.setDescription(challengeDTO.getDescription());

        LocalDate startDate = LocalDate.parse(challengeDTO.getStartDate(), DATE_FORMATTER);
        challenge.setStartDate(startDate.atStartOfDay());

    // YYYY.MM.DD형식으로 시작일 지정 후 지정한 기간에 따라 종료 기간 설정
    // CUSTOM은 시작일, 종료일 따로 설정
        switch (challengeDTO.getParticipateFrequency()) {
            case ONE_WEEK:
                challenge.setEndDate(startDate.plusWeeks(1).atTime(LocalTime.MAX));
                break;
            case TWO_WEEKS:
                challenge.setEndDate(startDate.plusWeeks(2).atTime(LocalTime.MAX));
                break;
            case ONE_MONTH:
                challenge.setEndDate(startDate.plusMonths(1).atTime(LocalTime.MAX));
                break;
            case CUSTOM:
                LocalDate endDate = LocalDate.parse(challengeDTO.getEndDate(), DATE_FORMATTER);
                challenge.setEndDate(endDate.atTime(LocalTime.MAX));
                break;
        }

        challenge.setParticipateFrequency(challengeDTO.getParticipateFrequency());
        challenge.setMaxParticipants(challengeDTO.getMaxParticipants());
        challenge.setTags(challengeDTO.getTags());

        Member member = memberRepository.findById(Integer.parseInt(challengeDTO.getCreatedBy()))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        challenge.setCreatedBy(member.getNickname());

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

    public List<ChallengeDTO> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        challenges.forEach(Challenge::updateStatus);
        return challenges.stream()
                .map(ChallengeDTO::fromEntity)
                .collect(Collectors.toList());
    }
    //마감순
    public List<ChallengeDTO> getChallengesEndingSoon() {
        List<Challenge> challenges = challengeRepository.findChallengesDeadline(LocalDateTime.now());
        return challenges.stream()
                .map(ChallengeDTO::fromEntity)
                .collect(Collectors.toList());
    }
    //인기순
    public List<ChallengeDTO> getPopularChallenges() {
        List<Challenge> challenges = challengeRepository.findAllByOrderByParticipantsDesc();
        return challenges.stream()
                .map(ChallengeDTO::fromEntity)
                .collect(Collectors.toList());
    }
    //최신순
    public List<ChallengeDTO> getNewestChallenges() {
        List<Challenge> challenges = challengeRepository.findAllByOrderByCreateDateDesc();
        return challenges.stream()
                .map(ChallengeDTO::fromEntity)
                .collect(Collectors.toList());
    }
}