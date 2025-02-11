package com.syudt.coslow.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserInfo {
    private String id;
    private String profileImg;
    private String nickname;
    private String accessToken;
    private String userId;


    public KakaoUserInfo(String id, String profileImg, String nickname, String accessToken, String userId) {
        this.id = id;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.userId = userId;
    }
}
