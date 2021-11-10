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
public class MultiLink {
    private Long id;
    private Account owner;
    private String name;
    private URI link;
    private Integer clicks;
    private Date addedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiLink)) return false;
        MultiLink multiLink = (MultiLink) o;
        return this.getId().equals(multiLink.getId()) &&
                this.getOwner().getId().equals(multiLink.getOwner().getId()) &&
                this.getName().equals(multiLink.getName()) &&
                this.getLink().toString().equals(multiLink.getLink().toString()) &&
                this.getAddedAt().toString().equals(multiLink.getAddedAt().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner().getId(), getName(), getLink().toString(), getAddedAt().toString());
    }
}
