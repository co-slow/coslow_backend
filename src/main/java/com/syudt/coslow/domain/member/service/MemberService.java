package com.syudt.coslow.domain.member.service;

import com.syudt.coslow.auth.dto.KakaoUserInfo;
import com.syudt.coslow.domain.member.dto.MemberSave;
import com.syudt.coslow.domain.member.entity.Member;
import com.syudt.coslow.domain.member.entity.UserRole;
import com.syudt.coslow.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

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
}

