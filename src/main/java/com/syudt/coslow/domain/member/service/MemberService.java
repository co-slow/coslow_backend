package com.syudt.coslow.domain.member.service;

import com.syudt.coslow.auth.dto.KakaoUserInfo;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import com.syudt.coslow.domain.challenge.repository.SaveChallengeRepository;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.entity.UserRole;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final SaveChallengeRepository saveChallengeRepository;

    public Integer saveMember(KakaoUserInfo kakaoUserInfo) {
        Member member = Member.builder()
                .oauthId(kakaoUserInfo.getId())
                .profileImg(kakaoUserInfo.getProfileImg())
                .nickname(kakaoUserInfo.getNickname())
                .userRole(UserRole.USER)
                .build();
        Member memberJustSaved = memberRepository.save(member);
        return memberJustSaved.getUserId();
    }

    public Optional<Member> findByOauthId(String oauthId) {
        return memberRepository.findByOauthId(oauthId);
    }

    public List<Challenge> getSavedChallengesByOauthId(String oauthId) {
        Member member = findByOauthId(oauthId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 OAuth ID입니다."));

        return saveChallengeRepository.findByMember(member).stream()
                .map(SaveChallenge::getChallenge)
                .collect(Collectors.toList());
    }
}
