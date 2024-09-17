package com.mamully.toyProject.users.command.repositories;

import com.mamully.toyProject.users.command.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserPhone(String userPhone);
    boolean existsByUserEmail(String userEmail);
    Optional<User> findByUserId(String userId);
}
