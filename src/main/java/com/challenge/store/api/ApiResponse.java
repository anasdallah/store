package com.challenge.store.api;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<R> {
    private String responseStatus;
    private Instant date;
    private String errorCode;
    private String errorMessage;
    private R responseBody;
    private Object errorDetails;
    private List<ValidationError> validationErrors;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ValidationError {
        private String fieldName;
        private String message;
    }
}
