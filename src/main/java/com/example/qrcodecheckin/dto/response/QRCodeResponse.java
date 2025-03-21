package com.example.qrcodecheckin.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class QRCodeResponse {
    Long id;
    String nonce;
    Instant createdAt;
    Instant expiresAt;
}
