package com.challenge.store.exception;


import com.challenge.store.constant.ApiErrors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException  {

    protected final HttpStatus status;
    protected final ApiErrors error;
    private final Object[] arguments;

    public ServiceException(HttpStatus status, ApiErrors error, Object... arguments) {
        super(new Exception());
        this.status = status;
        this.error = error;
        this.arguments = arguments;
    }

    @Override
    public String getMessage() {
        return String.format("Error code [%s], http status [%s]", error.getCode(), status.toString());
    }

    @Override
    public String toString() {
        return getMessage();
    }


    public static ServiceException badRequest(final ApiErrors error, final Object... args) {
        return new ServiceException(HttpStatus.BAD_REQUEST, error, args);
    }

    public static ServiceException invalidValue(final String fields) {
        return badRequest(ApiErrors.INVALID_FIELD_VALUE, fields);
    }

    public static ServiceException forbidden(final ApiErrors error, final Object... args) {
        return new ServiceException(HttpStatus.FORBIDDEN, error, args);
    }

    public static ServiceException unauthorized(final ApiErrors error, final Object... args) {
        return new ServiceException(HttpStatus.UNAUTHORIZED, error, args);
    }
}