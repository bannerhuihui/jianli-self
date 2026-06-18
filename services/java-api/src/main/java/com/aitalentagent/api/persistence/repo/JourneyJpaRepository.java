package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.JourneyRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JourneyJpaRepository extends JpaRepository<JourneyRow, String> {

    List<JourneyRow> findByUserIdOrderByUpdatedAtDesc(String userId);
}
