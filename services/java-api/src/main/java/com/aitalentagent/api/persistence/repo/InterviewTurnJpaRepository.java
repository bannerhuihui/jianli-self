package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.InterviewTurnRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTurnJpaRepository extends JpaRepository<InterviewTurnRow, String> {

    List<InterviewTurnRow> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    void deleteBySessionId(String sessionId);
}
