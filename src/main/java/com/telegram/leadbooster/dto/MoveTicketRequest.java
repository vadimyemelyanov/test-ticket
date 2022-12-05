package com.telegram.leadbooster.dto;

import com.telegram.leadbooster.dto.enums.TicketState;
import lombok.Data;

@Data
public class MoveTicketRequest {
    private TicketState targetState;
}
