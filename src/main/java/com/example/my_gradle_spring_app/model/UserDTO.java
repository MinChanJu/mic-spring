package com.example.my_gradle_spring_app.model;

import java.time.ZonedDateTime;

public class UserDTO {
    private Long id;
    private String name;
    private String userId;
    private String email;
    private Long authority;
    private ZonedDateTime createdAt;

    public UserDTO(Long id, String name, String userId, String email, Long authority, ZonedDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.authority = authority;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAuthority() {
        return authority;
    }

    public void setAuthority(Long authority) {
        this.authority = authority;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
