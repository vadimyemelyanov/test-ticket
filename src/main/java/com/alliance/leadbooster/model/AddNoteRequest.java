package com.alliance.leadbooster.model;

import lombok.Data;

import java.util.UUID;

@Data
public class AddNoteRequest {
    private UUID chatUuid;
    private String text;
}
