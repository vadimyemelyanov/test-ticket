package com.alliance.leadbooster.model;

import lombok.Data;

@Data
public class UpdateStateRequest {
    private String name;
    private String title;
    private String color;
    private Integer daysToComplete;
}
