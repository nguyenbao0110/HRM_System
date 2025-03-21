package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.LocationRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.LocationResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.service.LocationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {
    LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LocationResponse> createLocation(@RequestBody @Valid LocationRequest locationRequest) {
        return ApiResponse.success(locationService.createLocation(locationRequest), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<LocationResponse> getLocationById(@PathVariable Long id) {
        return ApiResponse.success(locationService.getLocationById(id), null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<LocationResponse>> getLocations(@RequestParam int page, int size) {
        return ApiResponse.success(locationService.getLocations(page, size), null);
    }

    @PutMapping("/{id}")
    public ApiResponse<LocationResponse> updateLocation(@PathVariable Long id, @RequestBody @Valid LocationRequest locationRequest) {
        return ApiResponse.success(locationService.updateLocation(id, locationRequest), null);
    }
}
