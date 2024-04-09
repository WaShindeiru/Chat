package org.example.chat.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "sentBy", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ChatMessage> messages;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;

    public ChatUser() {
        conversations = new ArrayList<>();
        authorities = new ArrayList<>();
    }

    public ChatUser(Long userId, String username, String password, UserStatus status) {
        this();
        this.id = userId;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public ChatUser(String username, String password) {
        this(username, password, UserStatus.OFFLINE);
    }

    public ChatUser(String username, String password, UserStatus status) {
        this(null, username, password, status);
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getUsers().add(this);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "id: " + id + " " + username + ", " + status;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this ) {
            return true;
        }

        if (obj instanceof ChatUser e) {
           return e.username.equals(this.username);
        }

        return false;
    }
}
