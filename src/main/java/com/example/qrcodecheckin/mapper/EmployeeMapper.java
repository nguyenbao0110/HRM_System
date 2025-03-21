package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.request.EmployeeRequest;
import com.example.qrcodecheckin.dto.response.EmployeeResponse;
import com.example.qrcodecheckin.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "employeeRequest.departmentId", target = "department.id")
    Employee toEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse toResponse(Employee employee);
    void updateEmployee(@MappingTarget Employee employee, EmployeeRequest employeeRequest);
}
