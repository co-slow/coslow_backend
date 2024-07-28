package com.syudt.coslow.domain.challenge.repository;

import com.syudt.coslow.domain.challenge.entity.SaveChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveChallengeRepository extends JpaRepository<SaveChallenge, Integer> {
}
