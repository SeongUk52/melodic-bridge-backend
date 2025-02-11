package com.melodicbridge.melodic_bridge.repository;

import com.melodicbridge.melodic_bridge.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
