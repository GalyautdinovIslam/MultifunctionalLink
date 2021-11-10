package ru.itis.models;

import lombok.*;

import java.net.URI;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutLink {
    private Long id;
    private Account owner;
    private String cut;
    private URI link;
    private Integer clicks;
    private Date addedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CutLink)) return false;
        CutLink cutLink = (CutLink) o;
        return this.getId().equals(cutLink.getId()) &&
                this.getOwner().getId().equals(cutLink.getOwner().getId()) &&
                this.getCut().equals(cutLink.getCut()) &&
                this.getLink().toString().equals(cutLink.getLink().toString()) &&
                this.getAddedAt().toString().equals(cutLink.getAddedAt().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner().getId(), getCut(), getLink().toString(), getAddedAt().toString());
    }
}
