package com.challenge.store.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class LoginRequest {
    @NotBlank
    @Size(min = 2, max = 25)
    private String username;

    @NotBlank
    @Size(min = 2, max = 25)
    private String password;

}
