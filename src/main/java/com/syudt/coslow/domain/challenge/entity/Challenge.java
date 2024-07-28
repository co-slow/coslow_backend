package com.syudt.coslow.domain.challenge.entity;

import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Integer challengeId;

    @ManyToMany
    @JoinTable(
            name = "challenge_participants",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Member> participants;

    @ElementCollection
    @CollectionTable(name = "challenge_tags", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "tag")
    private Set<String> tags;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "participate_frequency", nullable = false, columnDefinition = "ENUM('ONE_WEEK', 'TWO_WEEKS', 'ONE_MONTH', 'CUSTOM')")
    private ParticipateFrequency participateFrequency;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('RECRUITING', 'PROCEEDING', 'COMPLETED')")
    private ChallengeStatus status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "board_id", nullable = false)
    private Integer boardId;

    @Column(name = "createDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "lastModifiedDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate;

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            status = ChallengeStatus.RECRUITING;  // 모집중
        } else if (now.isAfter(endDate)) {
            status = ChallengeStatus.COMPLETED;  // 종료
        } else {
            status = ChallengeStatus.PROCEEDING;  // 진행중
        }
    }

    public enum ParticipateFrequency {
        ONE_WEEK("ONE_WEEK"),
        TWO_WEEKS("TWO_WEEKS"),
        ONE_MONTH("ONE_MONTH"),
        CUSTOM("CUSTOM");

        private final String value;

        ParticipateFrequency(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum ChallengeStatus {
        RECRUITING("RECRUITING"),
        PROCEEDING("PROCEEDING"),
        COMPLETED("COMPLETED");

        private final String value;

        ChallengeStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
