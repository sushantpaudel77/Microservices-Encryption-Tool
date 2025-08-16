package com.encryption_decryption_microservices.encryption_microservices_encryption_service.repository;

import com.encryption_decryption_microservices.encryption_microservices_encryption_service.entity.EncryptionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EncryptionRepository extends MongoRepository<EncryptionRecord, String> {
    List<EncryptionRecord> findAllByUserId(Long userId);
    List<EncryptionRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
}
