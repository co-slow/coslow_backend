package com.syudt.coslow.domain.member.entity;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.diet.entity.Diet;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @ManyToMany
    @JoinTable(
            name = "challenge_participants",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "challenge_id")
    )
    private Set<Challenge> challenges;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private Diet diet;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;


    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "challenge_count", columnDefinition = "INT DEFAULT 0")
    private Integer challengeCount;

    @Column(name = "char_point", columnDefinition = "INT DEFAULT 0")
    private Integer charPoint;

    @Column(name = "char_level", columnDefinition = "ENUM('1', '2', '3', '4', '5') DEFAULT '1'")
    private String charLevel;

    @Column(name = "createDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "lastModifiedDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate;
}