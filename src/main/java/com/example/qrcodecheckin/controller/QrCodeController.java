package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.QrCodeRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.QRCodeResponse;
import com.example.qrcodecheckin.service.QrCodeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QrCodeController {
    QrCodeService qrCodeService;

    @PostMapping
    public ApiResponse<QRCodeResponse> generateQRCode(@RequestBody @Valid QrCodeRequest qrCodeRequest) {
        return ApiResponse.success(qrCodeService.generateQRCode(qrCodeRequest), null);
    }
}
