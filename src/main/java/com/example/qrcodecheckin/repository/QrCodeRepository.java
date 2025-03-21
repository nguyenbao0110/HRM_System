package com.example.qrcodecheckin.repository;

import com.example.qrcodecheckin.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByIdAndNonce(Long id, String nonce);
    Optional<QrCode> findFirstByLocationId(Long id);
}
