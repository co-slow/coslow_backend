package com.syudt.coslow.domain.challenge.entity;
import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "savechallenge")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SaveChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "savechallenge_id")
    private int savechallengeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}