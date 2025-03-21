package com.example.qrcodecheckin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequest {
    @NotNull(message = "Username is required")
    @Size(min = 5, message = "Username must be as least 5 characters")
    String username;

    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password must be as least 5 characters")
    String password;
}
