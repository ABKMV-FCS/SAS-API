package com.sas.sasapi.repository;

import com.sas.sasapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(Long userID);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    User findByEmail(String email);

    Optional<Object> findByUsername(String username);
    @Query("select u from User u where u.username=?1")
    User getByUsername(String username);



    @Modifying
    @Transactional
    @Query("update User u set u.fcmToken=?1 where u.username=?2")
    void updateFcmToken(String fcmToken,String username);

    @Query("select u.fcmToken from User u where u.username=?1")
    String getFcmToken(String username);
}