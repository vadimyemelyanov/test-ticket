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
public class UpdateDealRequest {

    @NotNull
    private String dealUuid;
    private String name;
    private String product;

}
