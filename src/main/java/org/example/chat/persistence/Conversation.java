package org.example.chat.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String conversationName;
//    private String lastMessage;
//    private Long lastMessageUserId;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<ChatUser> users;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    public Conversation() {
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Conversation(String conversationName) {
        this();
        this.conversationName = conversationName;
    }

    public void addUser(ChatUser user) {
        this.users.add(user);
        user.getConversations().add(this);
    }

    public void removeUser(ChatUser user) {
        this.users.remove(user);
        user.getConversations().remove(this);
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        message.setOrigin(this);
    }

    public void removeMessage(ChatMessage message) {
        this.messages.remove(message);
        message.setOrigin(null);
    }

    @Override
    public String toString() {
        return this.id + ": " + this.conversationName;
    }
}
