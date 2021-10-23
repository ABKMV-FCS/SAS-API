package com.sas.sasapi.repository;

import com.sas.sasapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(Long userID);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Object> findByUsername(String username);
}