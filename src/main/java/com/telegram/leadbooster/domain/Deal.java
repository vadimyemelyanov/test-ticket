package com.telegram.leadbooster.domain;

import com.telegram.leadbooster.dto.enums.TicketState;
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
        name = "deal-with-notes-and-history",
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
@Table(name = "deals")
public class Deal {
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
    @JoinColumn(name = "deal_uuid")
    @BatchSize(size = 50)
    private Set<Notes> notes = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_uuid")
    @BatchSize(size = 50)
    private Set<StateHistory> stateHistory = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return uuid.equals(deal.uuid) && Objects.equals(name, deal.name) && Objects.equals(product, deal.product) && Objects.equals(telegramLink, deal.telegramLink) && currentState == deal.currentState && Objects.equals(authorUsername, deal.authorUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, product, telegramLink, currentState, authorUsername);
    }
}
