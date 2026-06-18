package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.TalentProfileRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TalentProfileJpaRepository extends JpaRepository<TalentProfileRow, String> {

    Optional<TalentProfileRow> findByJourneyId(String journeyId);
}
