package com.encryption_decryption_microservices.encryption_microservices_user_service.repository;

import com.encryption_decryption_microservices.encryption_microservices_user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
}
