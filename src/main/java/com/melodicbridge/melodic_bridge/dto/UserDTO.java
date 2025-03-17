package com.melodicbridge.melodic_bridge.dto;

import com.melodicbridge.melodic_bridge.domain.User;
import lombok.Getter;

@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}