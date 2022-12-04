package com.telegram.ticket.domain;

import com.telegram.ticket.dto.TicketState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "state_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateHistory {
    @Id
    @Column(name = "uuid")
    private String uuid = UUID.randomUUID().toString();
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TicketState state;
    @Column(name = "chat_uuid")
    private String chatUuid;
    @Column(name = "from_date")
    private LocalDateTime fromDate;
    @Column(name = "to_date")
    private LocalDateTime toDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateHistory that = (StateHistory) o;
        return Objects.equals(uuid, that.uuid) && state == that.state && Objects.equals(chatUuid, that.chatUuid) && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, state, chatUuid, fromDate, toDate);
    }
}
