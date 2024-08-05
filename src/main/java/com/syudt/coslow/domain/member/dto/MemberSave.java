package com.syudt.coslow.domain.member.dto;

import com.syudt.coslow.domain.member.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberSave {
    String oauthId;
    String profileImg;
    String nickname;
    UserRole userRole;
    String grade;
    int charPoint;
    int challengeCount;
    String charLevel;
}
