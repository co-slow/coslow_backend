package com.syudt.coslow.domain.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChallengeApplyDTO {
    private int challengeId;
    private int userId;

    public ChallengeApplyDTO(int challengeId, int userId) {
        this.challengeId = challengeId;
        this.userId = userId;
    }
}
