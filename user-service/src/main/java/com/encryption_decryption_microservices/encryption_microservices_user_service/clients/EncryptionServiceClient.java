package com.encryption_decryption_microservices.encryption_microservices_user_service.clients;

import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.EncryptionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "encryption-service", url = "http://localhost:8082")
public interface EncryptionServiceClient {

    @GetMapping("users/{userId}/history")
    List<EncryptionResponseDto> getUserEncryptionHistory(@PathVariable Long userId);
}

