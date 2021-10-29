package ru.itis.models;

import lombok.*;

import java.net.URL;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiLink {
    private Long id;
    private Account owner;
    private String link;
    private Integer clicks;
    private Date addedAt;
}
