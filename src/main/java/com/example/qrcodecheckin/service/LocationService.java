package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.LocationRequest;
import com.example.qrcodecheckin.dto.response.LocationResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.entity.Location;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.LocationMapper;
import com.example.qrcodecheckin.repository.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationService {
    LocationRepository locationRepository;
    LocationMapper locationMapper;

    public LocationResponse createLocation(LocationRequest request) {
        if (locationRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.LOCATION_EXISTED);
        }

        Location location = locationMapper.toLocation(request);
        locationRepository.save(location);

        return locationMapper.toResponse(location);
    }

    public LocationResponse getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXIST));
        return locationMapper.toResponse(location);
    }

    public PagedResponse<LocationResponse> getLocations(int page, int size) {
        Page<Location> pageResult = locationRepository.findAll(PageRequest.of(page - 1, size));
        return PagedResponse.<LocationResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(locationMapper::toResponse).toList())
                .build();
    }

    public LocationResponse updateLocation(Long id, LocationRequest request) {
        var location = locationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXIST));

        location.setName(request.getName());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());

        locationRepository.save(location);
        return locationMapper.toResponse(location);
    }
}
