package com.encryption_decryption_microservices.encryption_microservices_encryption_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class EncryptionMicroservicesEncryptionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncryptionMicroservicesEncryptionServiceApplication.class, args);
	}

}
