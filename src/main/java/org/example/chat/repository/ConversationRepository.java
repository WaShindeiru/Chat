package org.example.chat.repository;

import org.example.chat.persistence.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query
    ("""
    SELECT conv
    FROM Conversation conv
    JOIN conv.users user
    WHERE user.id = :id
    """)
    Collection<Conversation> getConversationsForUser(@Param("id") Long id);

    @Query
    ("""
    SELECT conv
    FROM Conversation conv
    WHERE conv.id = :id
    """)
    Optional<Conversation> findConversationById(@Param("id") Long id);

    @Query("""
    SELECT conv
    FROM Conversation conv
    """)
    Collection<Conversation> findAllConversations();

    @Query("""
    SELECT conv
    FROM Conversation conv
    WHERE conv.conversationName = :name
    """)
    Optional<Conversation> findConversationByName(@Param("name") String name);
}
