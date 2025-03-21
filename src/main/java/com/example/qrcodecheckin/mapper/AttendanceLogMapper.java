package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.response.AttendanceLogResponse;
import com.example.qrcodecheckin.entity.AttendanceLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceLogMapper {
    AttendanceLogResponse toResponse(AttendanceLog attendanceLog);
}
