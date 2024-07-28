package com.syudt.coslow.domain.challenge.repository;

import com.syudt.coslow.domain.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
}
