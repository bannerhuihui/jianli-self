package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.ResumeVersionRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeVersionJpaRepository extends JpaRepository<ResumeVersionRow, String> {

    List<ResumeVersionRow> findByJourneyIdOrderByGeneratedAtDesc(String journeyId);

    Optional<ResumeVersionRow> findFirstByJourneyIdAndVersionKeyOrderByGeneratedAtDesc(
            String journeyId,
            String versionKey
    );
}
