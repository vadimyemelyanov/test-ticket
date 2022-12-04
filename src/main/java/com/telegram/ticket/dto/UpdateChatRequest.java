package com.telegram.ticket.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class UpdateChatRequest {
    private UUID ticketUuid;
    private String name;
    private String product;
}
