package com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecryptionRequestDto {
    private String encryptedText;
    private String algorithm;
}