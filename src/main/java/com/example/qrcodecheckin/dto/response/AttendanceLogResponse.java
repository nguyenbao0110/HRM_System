package com.example.qrcodecheckin.dto.response;

import com.example.qrcodecheckin.enums.AttendanceStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceLogResponse {
    Long id;
    EmployeeResponse employee;
    LocalDateTime checkinTime;
    LocalDateTime checkoutTime;
    AttendanceStatus status;
    Double workHours;
}
