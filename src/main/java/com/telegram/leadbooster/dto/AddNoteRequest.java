package com.telegram.leadbooster.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddNoteRequest {
    private UUID chatUuid;
    private String text;
}
