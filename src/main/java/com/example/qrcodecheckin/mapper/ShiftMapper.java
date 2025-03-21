package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.response.ShiftResponse;
import com.example.qrcodecheckin.entity.Shift;
import org.mapstruct.Mapper;

@Mapper(componentModel =  "spring")
public interface ShiftMapper {
    ShiftResponse toResponse(Shift shift);
}
