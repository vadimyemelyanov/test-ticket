package com.telegram.ticket.dto;

import com.telegram.ticket.domain.Notes;
import com.telegram.ticket.domain.StateHistory;
import com.telegram.ticket.domain.Deal;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * A DTO for the {@link Deal} entity
 */
@Data
@Builder
public class TicketDto implements Serializable {
    private final String uuid;
    private final String name;
    private final String product;
    private final String telegramLink;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final String currentState;
    private final Set<Notes> notes;
    private final Set<StateHistory> stateHistories;
}