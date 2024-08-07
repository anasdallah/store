package com.challenge.store.controller;


import com.challenge.store.api.ApiResponse;
import com.challenge.store.api.ApiResponseBuilder;
import com.challenge.store.dto.BillRequest;
import com.challenge.store.dto.BillResponse;
import com.challenge.store.security.model.UserDetailsImpl;
import com.challenge.store.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("calculate")
    public ResponseEntity<ApiResponse<BillResponse>> calculateBill(@Valid @RequestBody BillRequest billRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ApiResponseBuilder.ok(billService.calculateBill(billRequest,userDetails));
    }
}
