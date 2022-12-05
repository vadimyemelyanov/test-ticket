package com.alliance.leadbooster.model;

import lombok.Data;


@Data
public class UpdateDealRequest {
    private String dealUuid;
    private String name;
    private String product;
}
