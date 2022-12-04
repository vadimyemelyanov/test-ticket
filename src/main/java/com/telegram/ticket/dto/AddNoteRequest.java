package com.telegram.ticket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddNoteRequest {
    private UUID chatUuid;
    private String text;
}
