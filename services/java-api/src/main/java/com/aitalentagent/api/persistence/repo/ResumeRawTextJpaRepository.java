package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.ResumeRawTextRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRawTextJpaRepository extends JpaRepository<ResumeRawTextRow, String> {

    Optional<ResumeRawTextRow> findByJourneyId(String journeyId);
}
