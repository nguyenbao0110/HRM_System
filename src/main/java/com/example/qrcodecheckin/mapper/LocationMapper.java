package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.request.LocationRequest;
import com.example.qrcodecheckin.dto.response.LocationResponse;
import com.example.qrcodecheckin.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationRequest locationRequest);
    LocationResponse toResponse(Location location);
}
