package ru.itis.models;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Set<MultiLink> multiLinks = new HashSet<>();
    private Set<CutLink> cutLinks = new HashSet<>();
    private Set<Account> subscriptions = new HashSet<>();
    private Set<Account> subscribers = new HashSet<>();
    private Date createdAt;

    public Account(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
