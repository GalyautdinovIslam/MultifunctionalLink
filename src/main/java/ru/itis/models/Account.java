package ru.itis.models;

import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private Integer age;
    //private String country;
    private Set<MultiLink> multiLinks = new HashSet<>();
    private Set<CutLink> cutLinks = new HashSet<>();
    private Set<Account> subscriptions = new HashSet<>();
    private Set<Account> subscribers = new HashSet<>();
    private Date createdAt;
    private Date editedAt;

    public Account(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
