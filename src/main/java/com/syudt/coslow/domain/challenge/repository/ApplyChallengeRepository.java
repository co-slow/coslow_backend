package com.syudt.coslow.domain.challenge.repository;

import com.syudt.coslow.domain.challenge.entity.ApplyChallenge;
import com.syudt.coslow.domain.challenge.entity.Challenge;
import com.syudt.coslow.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyChallengeRepository extends JpaRepository<ApplyChallenge, Integer> {
    boolean existsByChallengeAndMember(Challenge challenge, Member member);
    long countByChallenge(Challenge challenge);
    List<ApplyChallenge> findByMember(Member member);
}
