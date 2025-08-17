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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionServiceImpl implements EncryptionService{

    private final EncryptionRepository encryptionRepository;
    private final UserServiceClient userServiceClient;

    private final String AES_KEY = "MySecretKey12345";


    @Override
    public EncryptionResponseDto encryptText(EncryptionRequestDto request) {
        try {
            UserDto user = userServiceClient.getUserById(request.getUserId());
            log.info("Encrypting text for user: {}", user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }

        String encryptedText;

        try {
            encryptedText = switch (request.getAlgorithm().toUpperCase()) {
                case "AES" -> encryptAES(request.getText());
                case "BASE64" -> encryptBase64(request.getText());
                default -> throw new IllegalArgumentException("Unsupported algorithm: " + request.getAlgorithm());
            };
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed: " + e.getMessage());
        }

        EncryptionRecord record = new EncryptionRecord(
                request.getUserId(),
                request.getText(),
                encryptedText,
                request.getAlgorithm().toUpperCase()
        );

        EncryptionRecord saved = encryptionRepository.save(record);
        return mapToResponseDto(saved);
    }

    @Override
    public DecryptionResponseDto decryptionText(DecryptionRequestDto request) {
        String decryptedText;

        try {
            decryptedText = switch (request.getAlgorithm().toUpperCase()) {
                case "AES" -> decryptAES(request.getEncryptedText());
                case "BASE64" -> decryptBase64(request.getEncryptedText());
                default -> throw new IllegalArgumentException("Unsupported algorithm: " + request.getAlgorithm());
            };
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed: " + e.getMessage());
        }

        return new DecryptionResponseDto(decryptedText, request.getAlgorithm().toUpperCase());
    }

    @Override
    public List<EncryptionResponseDto> getUserHistory(Long userId) {
       try {
           UserDto user = userServiceClient.getUserById(userId);
           log.info("Fetching user history for user: {}", user.getUsername());
       } catch (Exception e) {
           throw new RuntimeException("User not found with id: " + userId);
       }

       return encryptionRepository.findByUserIdOrderByCreatedAtDesc(userId)
               .stream()
               .map(this::mapToResponseDto)
               .toList();
    }

    private String encryptAES(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decryptAES(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    private String encryptBase64(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    private String decryptBase64(String encryptedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        return new String(decodedBytes);
    }

    private EncryptionResponseDto mapToResponseDto(EncryptionRecord record) {
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
