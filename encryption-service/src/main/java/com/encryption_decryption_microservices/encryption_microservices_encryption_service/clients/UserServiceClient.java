package com.encryption_decryption_microservices.encryption_microservices_encryption_service.clients;

import com.encryption_decryption_microservices.encryption_microservices_encryption_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/users/username/{username}")
    UserDto getUserByUsername(@PathVariable String username);
}
