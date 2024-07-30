package com.syudt.coslow.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syudt.coslow.auth.dto.KakaoUserInfo;
import com.syudt.coslow.auth.service.AuthService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("hello")
    public String hello () {
        return "HELLOOOOOO";
    }

    //http://localhost:8080/oauth2/code/kakao?code=ltnb2PTBLGyaAymPFqTyI3mm-U3PHHb-MaJtE045iU_OMDO4bwAwEQAAAAQKPXNOAAABkP6N_SpDz1szkZmFRA
//    http://localhost:8080/oauth2/code/kakao?code=4Lz4SZBUPwNL7kDtSMkArCuu_ZkHZx3oGbufXXFlCM9oJNQSmlJAHQAAAAQKKw0gAAABkP6s16SQgW3aWXatGQ
    @GetMapping("/oauth2/code/kakao")
    public KakaoUserInfo kakaoAuth (@RequestParam("code") String code) throws JsonProcessingException {
        String token = authService.kakaoGetTokenViaCode(code);
        KakaoUserInfo kakaoUserInfo = authService.kakaoGetUserInfoViaToken(token);

        return kakaoUserInfo;
    }
}
