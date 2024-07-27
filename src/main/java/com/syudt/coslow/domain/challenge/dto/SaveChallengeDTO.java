package com.syudt.coslow.domain.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SaveChallengeDTO {
    private Integer challengeId;
    private Integer userId;

    public SaveChallengeDTO(Integer challengeId, Integer userId) {
        this.challengeId = challengeId;
        this.userId = userId;
    }
}
