package com.syudt.coslow.domain.diet.entity;

import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "savediet")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SaveDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_diet_id")
    private int saveDietId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;
}

