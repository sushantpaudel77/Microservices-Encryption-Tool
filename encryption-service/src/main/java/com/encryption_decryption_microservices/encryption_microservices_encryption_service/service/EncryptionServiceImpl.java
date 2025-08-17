package com.encryption_decryption_microservices.encryption_microservices_encryption_service.service;

import com.encryption_decryption_microservices.encryption_microservices_encryption_service.clients.UserServiceClient;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.*;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.entity.EncryptionRecord;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.repository.EncryptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionServiceImpl implements EncryptionService{

    private final EncryptionRepository encryptionRepository;
    private final UserServiceClient userServiceClient;

    private final String AES_KEY = "my_secret_key";

    @Override
    public EncryptionResponseDto encryptText(EncryptionRequestDto encryptionRequestDto) {
        verifyUserExists(encryptionRequestDto.getUserId());

        String encryptedText = encryptByAlgorithm(encryptionRequestDto.getText(), encryptionRequestDto.getAlgorithm());

        EncryptionRecord record = new EncryptionRecord(
                encryptionRequestDto.getUserId(),
                encryptionRequestDto.getText(),
                encryptedText,
                encryptionRequestDto.getAlgorithm()
        );

        EncryptionRecord savedRecord = encryptionRepository.save(record);
        return convertToEncryptionResponseDto(savedRecord);
    }

    @Override
    public DecryptionResponseDto decryptionText(DecryptionRequestDto decryptionRequestDto) {
        String decryptedText = decryptByAlgorithm(decryptionRequestDto.getEncryptedText(), decryptionRequestDto.getAlgorithm());
        return new DecryptionResponseDto(decryptedText, decryptionRequestDto.getAlgorithm().toUpperCase());
    }
    private void verifyUserExists(Long userId) {
        try {
            UserDto userDto = userServiceClient.getUserById(userId);
            log.info("Encrypting text for user: {}", userDto.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    private String encryptByAlgorithm(String text, String algorithm) {
        try {
            return switch (algorithm.toUpperCase()) {
                case "AES" -> encryptAES(text);
                case "BASE64" -> encryptBase64(text);
                default -> throw new RuntimeException("Invalid algorithm: " + algorithm);
            };
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting text: " + e.getMessage());
        }
    }

    private String decryptByAlgorithm(String text, String algorithm) {
        try {
            return switch (algorithm.toUpperCase()) {
                case "AES" -> decryptAES(text);
                case "BASE64" -> decryptBase64(text);
                default -> throw new RuntimeException("Invalid algorithm: " + algorithm);
            };
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting text: " + e.getMessage());
        }
    }

    private String encryptAES(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    private String decryptAES(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedByte);
    }

    private String encryptBase64(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    private String decryptBase64(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText));
    }

    private EncryptionResponseDto convertToEncryptionResponseDto(EncryptionRecord record) {
        return new EncryptionResponseDto(
                record.getId(),
                record.getUserId(),
                record.getOriginalText(),
                record.getEncryptedText(),
                record.getAlgorithm(),
                record.getCreatedAt()
        );
    }
}
