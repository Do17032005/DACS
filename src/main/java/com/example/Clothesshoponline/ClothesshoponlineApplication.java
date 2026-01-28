package com.example.Clothesshoponline;

import com.example.Clothesshoponline.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClothesshoponlineApplication {

	public static void main(String[] args) {
		// Load .env file TRƯỚC KHI khởi động Spring Boot
		// Static block trong DotenvConfig sẽ chạy khi class được load
		try {
			Class.forName("com.example.Clothesshoponline.config.DotenvConfig");
		} catch (ClassNotFoundException e) {
			System.err.println("Warning: Could not load DotenvConfig");
		}

		SpringApplication.run(ClothesshoponlineApplication.class, args);
	}

}
