package com.melodicbridge.melodic_bridge.repository;

import com.melodicbridge.melodic_bridge.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
