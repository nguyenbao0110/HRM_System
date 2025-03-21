package com.example.qrcodecheckin.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    Long id;
    String name;
    Double latitude;
    Double longitude;
}
