package com.encryption_decryption_microservices.encryption_microservices_user_service.service;

import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.CreateUserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserWithHistoryDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto createUserDto);

    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    List<UserDto> getAllUsers();

    void deleteUser(Long id);

    UserWithHistoryDto getUserWithHistory(Long id);
}
