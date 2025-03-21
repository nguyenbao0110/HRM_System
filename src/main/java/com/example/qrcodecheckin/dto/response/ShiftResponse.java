package com.example.qrcodecheckin.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftResponse {
    Long id;
    String name;
    LocalTime startTime;
    LocalTime endTime;
    Double requiredHours;
}
