package com.syudt.coslow.domain.challenge.entity;

import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "applychallenge")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ApplyChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applyChallengeId;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public enum Status {
        PENDING, APPROVED
    }
}
