package com.example.Clothesshoponline.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String email;
}
