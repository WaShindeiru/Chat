package org.example.chat.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public Conversation(Long id, String conversationName) {
        this();
        this.id = id;
        this.conversationName = conversationName;
    }

    public Conversation(String conversationName) {
        this(null, conversationName);
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

    @Override
    public int hashCode() {
        return this.conversationName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if (obj instanceof Conversation c) {
           return c.getConversationName().equals(this.conversationName);
        }

        return false;
    }
}
