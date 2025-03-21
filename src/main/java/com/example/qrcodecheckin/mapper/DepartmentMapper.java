package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.request.DepartmentRequest;
import com.example.qrcodecheckin.dto.response.DepartmentResponse;
import com.example.qrcodecheckin.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentRequest department);
    DepartmentResponse toResponse(Department department);
    void updateDepartment(@MappingTarget Department department, DepartmentRequest departmentRequest);
}
