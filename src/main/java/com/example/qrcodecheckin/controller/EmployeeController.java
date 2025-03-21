package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.EmployeeRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.EmployeeResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeController {
    EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        EmployeeResponse createdEmployee = employeeService.createEmployee(employeeRequest);
        return ApiResponse.success(createdEmployee, "Employee created successfully");
    }

    @GetMapping
    public ApiResponse<PagedResponse<EmployeeResponse>> getEmployees(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(employeeService.findAll(page, size), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployeeResponse> getEmployee(@PathVariable Long id) {
        return ApiResponse.success(employeeService.findEmployeeById(id), null);
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeRequest employeeRequest) {
        return ApiResponse.success(employeeService.updateEmployee(id, employeeRequest), "Employee updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
