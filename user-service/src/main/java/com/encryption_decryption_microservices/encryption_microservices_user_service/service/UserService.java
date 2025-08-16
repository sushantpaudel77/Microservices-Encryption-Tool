package com.encryption_decryption_microservices.encryption_microservices_user_service.service;

import com.encryption_decryption_microservices.encryption_microservices_user_service.clients.EncryptionServiceClient;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.CreateUserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.entity.User;
import com.encryption_decryption_microservices.encryption_microservices_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
