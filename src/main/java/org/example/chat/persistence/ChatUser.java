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
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    private Long useruid;
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

    public ChatUser(String username, String password) {
        this();
        this.username = username;
        this.status = UserStatus.OFFLINE;
        this.password = password;
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
}
