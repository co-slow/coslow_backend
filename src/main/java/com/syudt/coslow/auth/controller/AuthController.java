package com.syudt.coslow.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syudt.coslow.auth.dto.KakaoUserInfo;
import com.syudt.coslow.auth.service.AuthService;
import com.syudt.coslow.domain.member.dto.MemberSave;
import com.syudt.coslow.domain.member.entity.UserRole;
import com.syudt.coslow.domain.member.service.MemberService;
import com.syudt.coslow.global.util.S3Util;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final S3Util s3Util;

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
        memberService.saveMember(kakaoUserInfo);

        return kakaoUserInfo;
    }
//grant_type=authorization_code&client_id=5815a4975b35540b74e2ebbd27ed6902&redirect_uri=http://localhost:5173/loginredirect&code=k8GLMIWt3DKU17PLGHViRK4Jt7xJT2IyV82YQnMiS7Hu2HE49VP3BgAAAAQKPXObAAABkQvFi1rUNEQ5evY1pg
//    @GetMapping("/check-token")
//    public String checkToken(@RequestHeader("Authorization") String authHeader) throws IOException {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new IllegalArgumentException("잘못된 형식입니다.");
//        }
//
//        String token = authHeader.substring(7);
//        if (!authService.isTokenValid(token)) {
//            throw new IllegalArgumentException("유효하지않거나 만료된 토큰입니다.");
//        }
//
//        return "토큰이 유효합니다";
//    }

    @GetMapping("/oauth2/kakao/logout")
    public ResponseEntity logout(RequestEntity request) throws IOException {
        String accessToken = request.getHeaders().get("Authorization").toString().split(" ")[1].split("]")[0];

        if (authService.kakaoLogoutViaToken(accessToken)) {
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/img-upload")
    public ResponseEntity getPresignedUrlForUpload() {
        String presignedUrl = s3Util.generatePresignedUrlForUpload("random-grid");
        return ResponseEntity.ok().body(presignedUrl);
    }

    // 1. Get compressed image from frontend
    // 2. change image format
    // 3. send foodvisor api and get response
    // 4. upload image to S3 bucket
    @GetMapping("/img-collect")
    public ResponseEntity imgProcessing() {
        return ResponseEntity.status(200).body("image upload succeed");
    }
}