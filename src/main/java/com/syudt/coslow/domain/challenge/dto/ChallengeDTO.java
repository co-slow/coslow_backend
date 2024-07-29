package com.syudt.coslow.domain.challenge.dto;

import com.syudt.coslow.domain.challenge.entity.Challenge;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChallengeDTO {
    private String title;
    private String description;
    private String startDate; // YYYY.MM.DD 형식
    private String endDate; // YYYY.MM.DD 형식 (CUSTOM일 경우만 사용)
    private Challenge.ParticipateFrequency participateFrequency;
    private Integer maxParticipants;
    private Integer weeklyCheckInCount;
    private Set<String> tags;
    private String createdBy;
    private String daysRemaining; //D-n일
    private Challenge.ChallengeStatus status;

    public ChallengeDTO(String title, String description, String startDate, String endDate, Challenge.ParticipateFrequency participateFrequency, Integer maxParticipants, Integer weeklyCheckInCount, Set<String> tags, String createdBy, String daysRemaining, Challenge.ChallengeStatus status) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participateFrequency = participateFrequency;
        this.maxParticipants = maxParticipants;
        this.weeklyCheckInCount = weeklyCheckInCount;
        this.tags = tags;
        this.createdBy = createdBy;
        this.daysRemaining = daysRemaining;
        this.status = status;
    }

    public static ChallengeDTO fromEntity(Challenge challenge) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        ChallengeDTO dto = new ChallengeDTO();
        dto.setTitle(challenge.getTitle());
        dto.setDescription(challenge.getDescription());
        dto.setStartDate(challenge.getStartDate().format(formatter));
        dto.setEndDate(challenge.getEndDate().format(formatter));
        dto.setParticipateFrequency(challenge.getParticipateFrequency());
        dto.setMaxParticipants(challenge.getMaxParticipants());
        dto.setWeeklyCheckInCount(challenge.getWeeklyCheckInCount());
        dto.setTags(challenge.getTags());
        dto.setCreatedBy(challenge.getCreatedBy());
        dto.setDaysRemaining(challenge.getDaysRemaining());
        dto.setStatus(challenge.getStatus());
        return dto;
    }
}

