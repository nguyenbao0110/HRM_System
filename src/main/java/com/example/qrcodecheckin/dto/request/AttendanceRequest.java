package com.example.qrcodecheckin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttendanceRequest {
    @NotNull(message =  "Qr code id is required")
    Long qrCodeId;

    @NotNull(message = "Nonce is required")
    String nonce;

    @NotNull(message = "Latitude is required")
    double latitude;

    @NotNull(message = "Longitude is required")
    double longitude;
}
