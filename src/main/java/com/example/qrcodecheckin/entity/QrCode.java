package com.example.qrcodecheckin.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, length = 4)
    String nonce;

    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @Column(nullable = false)
    Instant expiresAt;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
