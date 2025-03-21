package com.example.qrcodecheckin.dto.response;

import com.example.qrcodecheckin.enums.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceResponse {
    LocalDateTime checkinTime;
    @Nullable
    LocalDateTime checkoutTime;
    AttendanceStatus status;
}
