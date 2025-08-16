package com.encryption_decryption_microservices.encryption_microservices_encryption_service.service;

import com.encryption_decryption_microservices.encryption_microservices_encryption_service.clients.UserServiceClient;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.EncryptionRequestDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.EncryptionResponseDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.UserDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.entity.EncryptionRecord;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.repository.EncryptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionService {

    private final EncryptionRepository encryptionRepository;
    private final UserServiceClient userServiceClient;

    private final String AES_KEY = "my_secret_key";

    public EncryptionResponseDto encryptText(EncryptionRequestDto encryptionRequestDto) {
        // verify user exists
        try {
            UserDto userDto = userServiceClient.getUserById(encryptionRequestDto.getUserId());
            log.info("Encrypting text for users: {}", userDto.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + encryptionRequestDto.getUserId());
        }

        String encryptedText;

        try {
            switch (encryptionRequestDto.getAlgorithm().toUpperCase()) {
                case "AES":
                    encryptedText = encryptAES(encryptionRequestDto.getText());
            }
        }
    }

    private String encryptAES(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    private String decryptAES(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(plainText));
        return new String(decryptedByte);
    }

    private String encryptBase64(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    private String decryptBase64(String plainText) {
       byte[] decodedBytes = Base64.getDecoder().decode(plainText);
       return new String(decodedBytes);
    }

    private EncryptionResponseDto convertToEncryptionResponseDto(EncryptionRecord record) {
        return new EncryptionResponseDto(record.getId(), record.getUserId(), record.getOriginalText(), record.getEncryptedText(), record.getAlgorithm(), record.getCreatedAt());
    }
}
