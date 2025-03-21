package com.example.qrcodecheckin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRequest {
    @NotNull(message = "Location name is required")
    @Length(min = 5, message = "Location name must be at least 5 characters long")
    String name;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Latitude must be between -90 and 90 degrees")
    @Max(value = 90, message = "Latitude must be between -90 and 90 degrees")
    Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Longitude must be between -180 and 180 degrees")
    @Max(value = 180, message = "Longitude must be between -180 and 180 degrees")
    Double longitude;
}


