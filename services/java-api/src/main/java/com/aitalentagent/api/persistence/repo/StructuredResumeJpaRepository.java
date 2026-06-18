package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.StructuredResumeRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StructuredResumeJpaRepository extends JpaRepository<StructuredResumeRow, String> {

    Optional<StructuredResumeRow> findByJourneyId(String journeyId);
}
