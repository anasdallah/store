package com.challenge.store.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApiErrors {

    INTERNAL_SERVER_ERROR("STORE-0001", "internal server error"),
    NOT_FOUND("STORE-0002", "{0} could not be found with the given parameters"),
    REQUIRED_FIELDS("STORE-0003", "These Fields Are Required: [{0}]"),
    INVALID_FIELD_VALUE("STORE-0004", "Invalid field value for field(s) {0}"),
    ACCESS_DENIED("STORE-0005", "Access Is Denied, {0}"),
    UNAUTHORIZED("STORE-0006", "{0}"),
    USERNAME_IS_EXISTS("STORE-0007", "Username is already exists: {0}"),
    VALIDATION_ERRORS("STORE-0008", "Validation error");


    private final String code;
    private final String description;
}