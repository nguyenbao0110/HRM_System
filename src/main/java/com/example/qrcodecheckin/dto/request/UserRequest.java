package com.example.qrcodecheckin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequest {
    @NotNull(message = "Firstname is required")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    String firstName;

    @NotNull(message = "Lastname is required")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    String lastName;

    @NotNull(message = "Username is required")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    String username;

    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    String password;

    Long employeeId;
}

