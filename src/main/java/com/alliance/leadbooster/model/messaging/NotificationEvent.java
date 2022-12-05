package com.alliance.leadbooster.model.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NotificationEvent {
    private NotificationEntity entity;
    private NotificationType type;
    private String uuid;
    private Map<String, String> newValues;
}
