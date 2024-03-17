package org.example.chat.repository;

import org.example.chat.persistence.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ChatUser, Long> {

    @Query("""
    SELECT user
    FROM ChatUser user
    WHERE user.username = :username
    """)
    Optional<ChatUser> findByUsername(@Param("username") String username);
}
