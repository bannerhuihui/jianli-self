package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.InterviewSessionRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewSessionJpaRepository extends JpaRepository<InterviewSessionRow, String> {

    Optional<InterviewSessionRow> findByJourneyId(String journeyId);
}
