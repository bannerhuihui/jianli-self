package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.RefreshTokenRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenRow, String> {
}
