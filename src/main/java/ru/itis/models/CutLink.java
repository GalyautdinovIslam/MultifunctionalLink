package ru.itis.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutLink {
    private Long id;
    private Account owner;
    private String cut;
    private String link;
    private Integer clicks;
    private Date addedAt;
}
