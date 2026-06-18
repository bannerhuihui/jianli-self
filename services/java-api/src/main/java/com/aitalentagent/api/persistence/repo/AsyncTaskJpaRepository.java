package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.AsyncTaskRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsyncTaskJpaRepository extends JpaRepository<AsyncTaskRow, String> {
}
