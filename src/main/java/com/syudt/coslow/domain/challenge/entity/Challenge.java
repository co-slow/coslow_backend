package com.syudt.coslow.domain.challenge.entity;
import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "participants_id")
    private Member participants;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "participate_frequency", nullable = false, columnDefinition = "ENUM('1 week', '2 weeks', '1 month', 'Custom')")
    private String participateFrequency;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('Recruiting', 'Proceeding', 'Completed')")
    private String status;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy;

    @Column(name = "createDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "lastModifiedDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate;
}
