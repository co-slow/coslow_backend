package com.syudt.coslow.domain.diet.entity;
import com.syudt.coslow.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diet")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diet_id")
    private Integer dietId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "diet_title", nullable = false)
    private String dietTitle;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "diet_img", nullable = false)
    private String dietImg;

    @Column(name = "createDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
}
