package com.encryption_decryption_microservices.encryption_microservices_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class EncryptionMicroservicesUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncryptionMicroservicesUserServiceApplication.class, args);
	}
}
