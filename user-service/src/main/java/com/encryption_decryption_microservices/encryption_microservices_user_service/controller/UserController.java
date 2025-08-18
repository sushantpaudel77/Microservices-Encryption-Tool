package com.encryption_decryption_microservices.encryption_microservices_user_service.controller;

import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.CreateUserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.dto.UserWithHistoryDto;
import com.encryption_decryption_microservices.encryption_microservices_user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserDto createUserDto) {
        var user = userService.createUser(createUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/with-history")
    public ResponseEntity<UserWithHistoryDto> getUserWithHistory(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithHistory(id));
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
