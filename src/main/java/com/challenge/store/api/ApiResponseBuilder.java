package com.challenge.store.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {

    public static <R> ResponseEntity<ApiResponse<R>> ok() {
        return ResponseEntity.ok(successResponse());
    }


    public static <R> ResponseEntity<ApiResponse<R>> ok(final R responseBody) {
        return ResponseEntity.ok(successResponse(responseBody));
    }


    public static <R> ResponseEntity<ApiResponse<R>> created(final R responseBody) throws URISyntaxException {
        return ResponseEntity.created(new URI("")).body(successResponse(responseBody));
    }

    private static <R> ApiResponse<R> successResponse() {
        return successResponse(null);
    }

    public static <R> ApiResponse<R> successResponse(final R responseBody) {

        ApiResponse<R> response = new ApiResponse<>();

        response.setResponseStatus("Success");
        response.setDate(Instant.now());
        response.setResponseBody(responseBody);

        return response;
    }
}
