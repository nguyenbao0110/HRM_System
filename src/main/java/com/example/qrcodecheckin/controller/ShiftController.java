package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.dto.response.ShiftResponse;
import com.example.qrcodecheckin.service.ShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping
    public ApiResponse<PagedResponse<ShiftResponse>> getShifts(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(shiftService.getShifts(page, size), null);
    }
}
