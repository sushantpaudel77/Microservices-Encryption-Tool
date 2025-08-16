package com.encryption_decryption_microservices.encryption_microservices_user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionResponseDto {
    private String id;
    private Long userId;
    private String originalText;
    private String encryptedText;
    private String algorithm;
    private LocalDateTime createdAt;
}