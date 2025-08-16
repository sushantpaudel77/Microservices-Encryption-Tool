package com.encryption_decryption_microservices.encryption_microservices_user_service.service;

import com.encryption_decryption_microservices.encryption_microservices_user_service.clients.EncryptionServiceClient;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.CreateUserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserWithHistoryDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.entity.User;
import com.encryption_decryption_microservices.encryption_microservices_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionServiceClient encryptionServiceClient;

    public UserDto creteUser(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new RuntimeException("User already exists with username: " + createUserDto.getUsername());
        }

        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());

        User saveduser = userRepository.save(user);
        log.info("User created: {}", saveduser.getUsername());

        return mapToUserDto(saveduser);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .toList();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    // Get user with their encryption history using Feign Client
    public UserWithHistoryDto getUserWithHistory(Long id) {
        UserDto userDto = getUserById(id);
         try {
             var encryptionHistory = encryptionServiceClient.getUserEncryptionHistory(id);
             return new UserWithHistoryDto(userDto, encryptionHistory);
         } catch (Exception e) {
             log.error("Error getting user encryption history: {}", e.getMessage());
             return new UserWithHistoryDto(userDto, List.of());
         }
    }

    private UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
