package com.melodicbridge.melodic_bridge.service;

import com.melodicbridge.melodic_bridge.repository.GroupMemberRepository;
import com.melodicbridge.melodic_bridge.repository.GroupRepository;
import com.melodicbridge.melodic_bridge.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository,
                        GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public void addMemberToGroup() {
    }
}
