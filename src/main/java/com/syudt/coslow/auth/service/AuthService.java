package com.syudt.coslow.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syudt.coslow.auth.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@Transactional
public class AuthService {
    @Value("${kakao.app_key}")
    private String clientId;

    @Value("${kakao.redirect_url}")
    private String redirectUri;

    public String kakaoGetTokenViaCode(String code) throws JsonProcessingException {
        String url = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfo kakaoGetUserInfoViaToken(String token) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        System.out.println(jsonNode);

        String id = jsonNode.get("id").asText();
        String profileImg = jsonNode.get("properties").get("profile_image").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        return new KakaoUserInfo(id, profileImg, nickname, token);
    }
    public String isTokenValid(String token) throws IOException {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        int responseCode = response.getStatusCodeValue();

        if (responseCode != 200) {
            throw new IOException();
        }

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long expiresIn = jsonNode.get("expires_in").asLong();
        String oauthId = String.valueOf(jsonNode.get("id").asLong());

        return oauthId;
    }

    public Boolean kakaoLogoutViaToken(String accessToken) throws IOException {
        String url = "https://kapi.kakao.com/v1/user/logout";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", Long.parseLong(isTokenValid(accessToken)));

        HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        int responseCode = response.getStatusCodeValue();

        if (responseCode == 200) {
            return true;
        } else {
            return false;
        }
    }
}