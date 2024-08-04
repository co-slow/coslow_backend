package com.syudt.coslow.domain.challenge.repository;

import com.syudt.coslow.domain.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    // 인기순 (저장 많이한 순)
    List<Challenge> findAllByOrderByParticipantsDesc();

    // 최신순
    List<Challenge> findAllByOrderByCreateDateDesc();

//    현재 날짜를 기준으로 마감일이 얼마 안 남은 순으로 정렬, 종료된 챌린지는 그 뒤로 보냄
    @Query("SELECT c FROM Challenge c ORDER BY CASE WHEN c.endDate >= :now THEN 0 ELSE 1 END, c.endDate ASC")
    List<Challenge> findChallengesDeadline(@Param("now") LocalDateTime now);

    List<Challenge> findByCreatedBy(String createdBy);
}
