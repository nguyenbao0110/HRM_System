package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.response.QRCodeResponse;
import com.example.qrcodecheckin.entity.QrCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {
    QRCodeResponse toResponse(QrCode qrCode);
}
