package org.example.chat.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.chat.dto.ChatMessageDtoWithoutId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String messageText;
    private LocalDateTime sentDateTime;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by_user_id")
    private ChatUser sentBy;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation origin;

    public ChatMessage(String messageText, LocalDateTime sentDateTime) {
        this.messageText = messageText;
        this.sentDateTime = sentDateTime;
    }

    public ChatMessage(String messageText, LocalDateTime sentDateTime, ChatUser sentBy, Conversation origin) {
        this.messageText = messageText;
        this.sentDateTime = sentDateTime;
        this.sentBy = sentBy;
        this.origin = origin;
    }

    public ChatMessage(ChatMessageDtoWithoutId message) {
        this.messageText = message.getMessageText();
        this.sentDateTime = message.getSentDateTime();
    }

    public void setMessageSender(ChatUser user) {
        this.sentBy = user;
        user.getMessages().add(this);
    }

    public void removeMessageSender(ChatUser user) {
        this.sentBy = null;
        user.getMessages().remove(this);
    }

    public void setMessageConversation(Conversation conversation) {
        this.origin = conversation;
        conversation.getMessages().add(this);
    }

    public void removeMessageConversation(Conversation conversation) {
        this.origin = null;
        conversation.getMessages().remove(this);
    }

    @Override
    public String toString() {
        return this.id + ", " + this.messageText + ", " + this.sentDateTime;
    }
}
