package com.example.qrcodecheckin.dto.request;

import com.example.qrcodecheckin.enums.EmploymentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeRequest {
    @NotNull(message = "Firstname is required")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    String firstName;

    @NotNull(message = "Lastname is required")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be a date in the past")
    LocalDate dateOfBirth;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format, please enter a valid email (example@example.com)")
    String email;

    @NotNull(message = "Employment type is required")
    EmploymentType employmentType;

    @NotNull(message = "Department id is required")
    Long departmentId;
}

