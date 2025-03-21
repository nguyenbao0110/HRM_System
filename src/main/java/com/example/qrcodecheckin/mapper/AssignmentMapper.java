package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.response.AssignmentResponse;
import com.example.qrcodecheckin.entity.Assignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    AssignmentResponse toResponse(Assignment assignment);
}
