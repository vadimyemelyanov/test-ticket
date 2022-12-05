package com.telegram.leadbooster.web.advice;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class ApiError {
    private String message;
    private String errorCode;
    private HashMap<String, String> errorParameters;
}
