package com.telegram.ticket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notes {
    @Id
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();
    @Column(name = "deal_uuid")
    private String dealUuid;
    @Column(name = "text")
    private String text;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return Objects.equals(uuid, notes.uuid) && Objects.equals(text, notes.text) && Objects.equals(createdAt, notes.createdAt) && Objects.equals(updatedAt, notes.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, text, createdAt, updatedAt);
    }
}
