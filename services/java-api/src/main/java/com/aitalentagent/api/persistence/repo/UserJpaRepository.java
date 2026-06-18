package com.aitalentagent.api.persistence.repo;

import com.aitalentagent.api.persistence.entity.UserRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserRow, String> {

    Optional<UserRow> findByDeviceId(String deviceId);
}
