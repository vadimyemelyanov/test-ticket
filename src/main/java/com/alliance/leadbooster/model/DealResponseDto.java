package com.alliance.leadbooster.model;

import com.alliance.leadbooster.model.enums.DealState;
import com.alliance.leadbooster.persistence.entity.Notes;
import com.alliance.leadbooster.persistence.entity.StateHistory;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link com.alliance.leadbooster.persistence.entity.Deal} entity
 */
@Data
@Builder
public class DealResponseDto implements Serializable {
    private final String uuid;
    private final String name;
    private final String product;
    private final String telegramLink;
    private final Ticket ticket;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Set<Notes> notes;
    private final Set<StateHistory> stateHistory;

    @Data
    @Builder
    public static class Ticket implements Serializable {
        private final String dealUuid;
        private final DealState state;
        private final String authorUsername;
        private final String lastMessage;
        private final LocalDateTime lastMessageReceivedAt;
        private final LocalDateTime fromDate;
    }
}