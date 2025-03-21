package com.example.qrcodecheckin.dto.response;

import com.example.qrcodecheckin.enums.EmploymentType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse implements Serializable {
    Long id;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    EmploymentType employmentType;
    DepartmentResponse department;
}
