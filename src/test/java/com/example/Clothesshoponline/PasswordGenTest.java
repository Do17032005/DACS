package com.example.Clothesshoponline;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenTest {
    @Test
    public void generate() {
        System.out.println("HASH_START");
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
        System.out.println("HASH_END");
    }
}
