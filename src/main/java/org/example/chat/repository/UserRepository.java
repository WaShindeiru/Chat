package org.example.chat.repository;

import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ChatUser, Long> {

    @Query("""
    SELECT user
    FROM ChatUser user
    WHERE user.username = :username
    """)
    Optional<ChatUser> findByUsername(@Param("username") String username);

    @Query("""
    SELECT user
    FROM Conversation conv
    JOIN conv.users user
    WHERE user.username = :username
    """)
    Optional<Conversation> getConversationForUserByUsername(@Param("username") String username);

    @Query("""
    SELECT user
    FROM Conversation conv
    JOIN conv.users user
    WHERE conv.id = :id
    """)
    Collection<ChatUser> getUserForConversationById(@Param("id") long id);

    @Query("""
    SELECT user
    FROM Conversation conv
    JOIN conv.users user
    WHERE conv = :conv_
    """)
    Collection<ChatUser> getUserForConversation(@Param("conv_") Conversation conv_);
}
