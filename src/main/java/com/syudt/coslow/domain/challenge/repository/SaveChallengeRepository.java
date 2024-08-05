package com.syudt.coslow.domain.challenge.repository;

import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import com.syudt.coslow.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveChallengeRepository extends JpaRepository<SaveChallenge, Integer> {
    List<SaveChallenge> findByMember(Member member);
}
