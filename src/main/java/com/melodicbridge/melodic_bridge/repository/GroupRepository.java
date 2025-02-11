package com.melodicbridge.melodic_bridge.repository;

import com.melodicbridge.melodic_bridge.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
