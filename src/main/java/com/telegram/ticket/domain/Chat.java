package com.telegram.ticket.domain;

import com.telegram.ticket.dto.TicketState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "chats-with-notes-and-history",
        attributeNodes = {
            @NamedAttributeNode(value = "notes"),
            @NamedAttributeNode(value = "stateHistory")
        }
    )
})
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class Chat {
    @Id
    @Column(name = "uuid")
    private String uuid = "TCT-" + UUID.randomUUID();
    @Column(name = "name")
    private String name;
    @Column(name = "product")
    private String product;
    @Column(name = "telegram_link")
    private String telegramLink;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "current_state")
    @Enumerated(EnumType.STRING)
    private TicketState currentState;

    @Column(name = "author_username")
    private String authorUsername;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "last_message_received_at")
    private LocalDateTime lastMessageReceivedAt;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_uuid")
    @BatchSize(size = 50)
    private Set<Notes> notes = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_uuid")
    @BatchSize(size = 50)
    private Set<StateHistory> stateHistory = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(uuid, chat.uuid) && Objects.equals(name, chat.name) && Objects.equals(product, chat.product) && Objects.equals(telegramLink, chat.telegramLink) && Objects.equals(createdAt, chat.createdAt) && Objects.equals(updatedAt, chat.updatedAt) && currentState == chat.currentState && Objects.equals(authorUsername, chat.authorUsername) && Objects.equals(lastMessage, chat.lastMessage) && Objects.equals(telegramChatId, chat.telegramChatId) && Objects.equals(lastMessageReceivedAt, chat.lastMessageReceivedAt) && Objects.equals(notes, chat.notes) && Objects.equals(stateHistory, chat.stateHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, product, telegramLink, createdAt, updatedAt, currentState, authorUsername, lastMessage, telegramChatId, lastMessageReceivedAt, notes, stateHistory);
    }
}
