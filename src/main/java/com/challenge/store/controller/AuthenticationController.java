package com.challenge.store.controller;


import java.net.URISyntaxException;

import com.challenge.store.api.ApiResponse;
import com.challenge.store.api.ApiResponseBuilder;
import com.challenge.store.dto.LoginRequest;
import com.challenge.store.dto.LoginResponse;
import com.challenge.store.dto.RegisterRequest;
import com.challenge.store.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity
            <ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {

        return ApiResponseBuilder.ok(authenticationService.login(loginRequest));

    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) throws URISyntaxException {
        return ApiResponseBuilder.created(authenticationService.register(request));
    }

}
