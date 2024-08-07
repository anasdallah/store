package com.challenge.store.exception;


import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.nio.file.AccessDeniedException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

import com.challenge.store.api.ApiResponse;
import com.challenge.store.constant.ApiErrors;
import com.challenge.store.security.AuthEntryPoint;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public <R> ResponseEntity<ApiResponse<R>> handleServiceException(final ServiceException ex) {

        log.error("Service Exception Occurred: " + MessageFormat.format(ex.getError().getDescription(), ex.getArguments()));

        var error = ex.getError();

        ApiResponse<R> response = new ApiResponse<>();

        response.setResponseStatus("Failed");
        response.setDate(Instant.now());
        response.setErrorCode(error.getCode());
        response.setErrorMessage(MessageFormat.format(error.getDescription(), ex.getArguments()));

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(value = {RuntimeException.class, UnsupportedOperationException.class, IllegalStateException.class})
    public <R> ResponseEntity<ApiResponse<R>> runTimeException(Exception ex) {

        log.error(ex.getMessage());

        var error = ApiErrors.INTERNAL_SERVER_ERROR;

        ApiResponse<R> response = new ApiResponse<>();

        response.setResponseStatus("Failed");
        response.setDate(Instant.now());
        response.setErrorCode(error.getCode());
        response.setErrorMessage(error.getDescription());

        return ResponseEntity.internalServerError().body(response);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<ApiResponse.ValidationError> validationErrors =
                ex.getBindingResult().getAllErrors()
                        .stream()
                        .map(err ->
                                     new ApiResponse
                                             .ValidationError(((FieldError) err).getField(),
                                                              err.getDefaultMessage()))
                        .toList();

        ApiResponse<Object> response = new ApiResponse<>();

        var error = ApiErrors.VALIDATION_ERRORS;

        response.setResponseStatus("Failed");
        response.setDate(Instant.now());
        response.setErrorCode(error.getCode());
        response.setErrorMessage(error.getDescription());
        response.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(value = {AuthenticationException.class, JwtException.class})
    public <R> ResponseEntity<ApiResponse<R>> authenticationException(ServiceException ex) {


        var error = ex.getError();

        ApiResponse<R> response = new ApiResponse<>();

        response.setResponseStatus("Failed");
        response.setDate(Instant.now());
        response.setErrorCode(error.getCode());
        response.setErrorMessage(MessageFormat.format(error.getDescription(), ex.getArguments()));

        return ResponseEntity.status(UNAUTHORIZED).body(response);

    }

    /**
     * No usage for now, If there's an API that depends on roles will be used, by adding it to the
     * {@link AuthEntryPoint} AuthEntryPoint
     *
     * @return empty body
     */
    //
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> accessDenied(Exception e) {
        ServiceException serviceException = ServiceException.unauthorized(ApiErrors.UNAUTHORIZED, e.getMessage());
        return authenticationException(serviceException);
    }
}
