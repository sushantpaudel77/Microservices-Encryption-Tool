package com.encryption_decryption_microservices.encryption_microservices_encryption_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Inherited;
import java.time.LocalDateTime;

@Document(collection = "encryption_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionRecord {
    
    @Id
    private String id;

    private Long userId;
    private String originalText;
    private String encryptedText;
    private String algorithm;

    @CreatedDate
    private LocalDateTime createdAt;

    public EncryptionRecord(Long userId, String originalText, String encryptedText, String algorithm) {
        this.userId = userId;
        this.originalText = originalText;
        this.encryptedText = encryptedText;
        this.algorithm = algorithm;
        this.createdAt = LocalDateTime.now();
    }
}
