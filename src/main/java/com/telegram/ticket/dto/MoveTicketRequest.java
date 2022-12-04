package com.telegram.ticket.dto;

import lombok.Data;

@Data
public class MoveTicketRequest {
    private TicketState targetState;
}
