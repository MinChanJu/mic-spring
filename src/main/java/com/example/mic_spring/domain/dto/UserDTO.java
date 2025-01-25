package com.example.mic_spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

import com.example.mic_spring.domain.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String userId;
    private String email;
    private Short authority;
    private ZonedDateTime createdAt;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.authority = user.getAuthority();
        this.createdAt = user.getCreatedAt();
    }
}
