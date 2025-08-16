package com.encryption_decryption_microservices.encryption_microservices_user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithHistoryDto {
    // UserWithHistoryDto.java
    private UserDto user;
    private List<EncryptionResponseDto> encryptionHistory;
}