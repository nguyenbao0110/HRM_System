package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.response.AttendanceResponse;
import com.example.qrcodecheckin.entity.AttendanceLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    AttendanceResponse toResponse(AttendanceLog attendanceLog);
}
