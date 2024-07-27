package com.syudt.coslow.domain.challenge.dto;

import com.syudt.coslow.domain.challenge.entity.Challenge;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChallengeDTO {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Challenge.ParticipateFrequency participateFrequency;
    private Integer maxParticipants;
    private Set<String> tags;
    private String createdBy;
    private String daysRemaining;
    private Challenge.ChallengeStatus status;

    public ChallengeDTO(String title, String description, LocalDateTime startDate, LocalDateTime endDate, Challenge.ParticipateFrequency participateFrequency, Integer maxParticipants, Set<String> tags, String createdBy, String daysRemaining, Challenge.ChallengeStatus status) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participateFrequency = participateFrequency;
        this.maxParticipants = maxParticipants;
        this.tags = tags;
        this.createdBy = createdBy;
        this.daysRemaining = daysRemaining;
        this.status = status;
    }
}
