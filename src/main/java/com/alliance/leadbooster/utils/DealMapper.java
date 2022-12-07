package com.alliance.leadbooster.utils;

import com.alliance.leadbooster.model.DealResponseDto;
import com.alliance.leadbooster.persistence.entity.Deal;

public class DealMapper {

    public static DealResponseDto mapToDto(Deal deal) {
        return DealResponseDto.builder()
            .uuid(deal.getUuid())
            .name(deal.getName())
            .product(deal.getProduct())
            .telegramLink(deal.getTelegramLink())
            .ticket(DealResponseDto.Ticket.builder()
                .dealUuid(deal.getUuid())
                .lastMessage(deal.getLastMessage())
                .state(deal.getCurrentState())
                .authorUsername(deal.getAuthorUsername())
                .lastMessageReceivedAt(deal.getLastMessageReceivedAt())
                .fromDate(deal.getUpdatedAt())
                .build())
            .stateHistory(deal.getStateHistory())
            .notes(deal.getNotes())
            .createdAt(deal.getCreatedAt())
            .updatedAt(deal.getUpdatedAt())
            .build();
    }
}
