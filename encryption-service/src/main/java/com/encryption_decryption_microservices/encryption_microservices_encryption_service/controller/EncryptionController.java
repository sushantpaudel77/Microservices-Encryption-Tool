package com.encryption_decryption_microservices.encryption_microservices_encryption_service.controller;

import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.DecryptionRequestDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.DecryptionResponseDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.EncryptionRequestDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.EncryptionResponseDto;
import com.encryption_decryption_microservices.encryption_microservices_encryption_service.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5172")
@RequestMapping("/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponseDto> encryptText(@RequestBody EncryptionRequestDto encryptionRequestDto) {
        var response = encryptionService.encryptText(encryptionRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<DecryptionResponseDto> decryptText(@RequestBody DecryptionRequestDto decryptionRequestDto) {
        DecryptionResponseDto responseDto = encryptionService.decryptText(decryptionRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/users/{userId}/history")
    public ResponseEntity<List<EncryptionResponseDto>> getUserHistory(@PathVariable Long userId) {
        List<EncryptionResponseDto> history = encryptionService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Encryption Service is running");
    }
}
