package com.alliance.leadbooster.model;

import com.alliance.leadbooster.model.enums.TicketState;
import lombok.Data;

@Data
public class MoveTicketRequest {
    private TicketState targetState;
}
