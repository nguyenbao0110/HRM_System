package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.AttendanceRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.AttendanceLogResponse;
import com.example.qrcodecheckin.dto.response.AttendanceResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AttendanceController {
    AttendanceService attendanceService;

    @PostMapping
    public ApiResponse<AttendanceResponse> attendance(@RequestBody @Valid AttendanceRequest attendanceRequest) {
        return ApiResponse.success(attendanceService.attendance(attendanceRequest), null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<AttendanceLogResponse>> getAttendanceLogs(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(attendanceService.getAttendanceLogs(page, size), null);
    }
}
