package com.example.qrcodecheckin.enums;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ShiftEnum {
    MORNING("Morning", LocalTime.of(8, 0), LocalTime.of(12, 0), 4.0),
    AFTERNOON("Afternoon", LocalTime.of(13, 0), LocalTime.of(17, 0), 4.0),
    NIGHT("Night", LocalTime.of(17, 0), LocalTime.of(22, 0), 5.0),
    FULL_DAY("Full Day", LocalTime.of(8, 0), LocalTime.of(17, 0), 8.0);

    String name;
    LocalTime startTime;
    LocalTime endTime;
    double requiredHours;
}
