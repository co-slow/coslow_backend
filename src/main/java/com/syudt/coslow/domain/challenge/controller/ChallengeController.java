package com.syudt.coslow.domain.challenge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChallengeController {
    @GetMapping("hello")
    public String hello () {
        return "Helloooooo";
    }
}
