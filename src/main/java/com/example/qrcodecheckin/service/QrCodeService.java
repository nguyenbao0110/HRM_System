package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.QrCodeRequest;
import com.example.qrcodecheckin.dto.response.QRCodeResponse;
import com.example.qrcodecheckin.entity.Location;
import com.example.qrcodecheckin.entity.QrCode;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.QrCodeMapper;
import com.example.qrcodecheckin.repository.LocationRepository;
import com.example.qrcodecheckin.repository.QrCodeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QrCodeService {
    QrCodeMapper qrCodeMapper;
    QrCodeRepository qrCodeRepository;
    LocationRepository locationRepository;
    SecureRandom secureRandom = new SecureRandom();

    public QRCodeResponse generateQRCode(QrCodeRequest qrCodeRequest) {
        Location location = locationRepository
                .findById(qrCodeRequest.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXIST));
        Optional<QrCode> existingQrCodeOpt = qrCodeRepository.findFirstByLocationId(location.getId());
        int EXPIRED_MINUTES = 2;
        if (existingQrCodeOpt.isPresent()) {
            QrCode existingQrCode = existingQrCodeOpt.get();
            //Existed and not expired -> update expires at
            if (existingQrCode.getExpiresAt().isAfter(Instant.now())) {
                existingQrCode.setExpiresAt(Instant.now().plus(EXPIRED_MINUTES, ChronoUnit.MINUTES));
                qrCodeRepository.save(existingQrCode);
                return qrCodeMapper.toResponse(existingQrCode);
            } else {
                qrCodeRepository.delete(existingQrCode);
            }
        }
        //Not exist -> create new
        QrCode newQrCode = QrCode.builder()
                .location(location)
                .nonce(String.format("%04d", secureRandom.nextInt(10000)))
                .expiresAt(Instant.now().plus(EXPIRED_MINUTES, ChronoUnit.MINUTES))
                .build();
        return qrCodeMapper.toResponse(qrCodeRepository.save(newQrCode));
    }
}
