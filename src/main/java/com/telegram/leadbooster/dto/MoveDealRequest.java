package com.telegram.leadbooster.dto;

import com.telegram.leadbooster.dto.enums.DealState;
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
public class MoveDealRequest {
    @NotNull
    private DealState targetState;
}
