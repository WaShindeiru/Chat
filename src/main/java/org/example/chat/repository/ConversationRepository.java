package org.example.chat.repository;

import org.example.chat.persistence.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query
    ("""
    SELECT conv
    FROM Conversation conv
    JOIN conv.users user 
    WHERE user.id = :id
    """)
    Collection<Conversation> getConversationsForUser(@Param("id") Long id);
}
