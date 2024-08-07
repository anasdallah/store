package com.challenge.store.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class RegisterRequest {

    @NotBlank
    @Size(min = 2, max = 25)
    private String username;

    @NotBlank
    @Size(min = 8, max = 60)
    private String password;

    private Set<String> roles;

}
