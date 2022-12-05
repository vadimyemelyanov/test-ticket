package com.alliance.leadbooster.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddNoteRequest {
    @NotNull
    private String dealUuid;
    @NotNull
    private String content;
}
