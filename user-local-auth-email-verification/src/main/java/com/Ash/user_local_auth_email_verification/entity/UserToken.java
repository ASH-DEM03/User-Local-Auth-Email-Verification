package com.Ash.user_local_auth_email_verification.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "user_token")
@Getter
@Setter
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String token;

}
