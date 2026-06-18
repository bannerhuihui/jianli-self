package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.ResumeFileRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeFileJpaRepository extends JpaRepository<ResumeFileRow, String> {

    Optional<ResumeFileRow> findFirstByJourneyIdOrderByUploadedAtDesc(String journeyId);
}
