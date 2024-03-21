package org.example.chat.repository;

import org.example.chat.persistence.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query
    ("""
    SELECT message
    FROM ChatMessage message 
    WHERE message.origin.id = :id
    """)
    public Collection<ChatMessage> getMessagesFromConversation(@Param("id") Long conversationId);
}
