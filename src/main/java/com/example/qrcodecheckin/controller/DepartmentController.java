package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.DepartmentRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.DepartmentResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.qrcodecheckin.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DepartmentController {
    DepartmentService departmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DepartmentResponse> createDepartment(@RequestBody @Valid DepartmentRequest departmentRequest) {
        DepartmentResponse createdDepartment = departmentService.createDepartment(departmentRequest);
        return ApiResponse.success(createdDepartment, null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<DepartmentResponse>> findAll(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(departmentService.findAll(page, size), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<DepartmentResponse> findById(@PathVariable Long id) {
        return ApiResponse.success(departmentService.findDepartmentById(id), null);
    }

    @PutMapping("/{id}")
    public ApiResponse<DepartmentResponse> updateDepartment(@PathVariable Long id, @RequestBody @Valid DepartmentRequest departmentRequest) {
        return ApiResponse.success(departmentService.updateDepartment(id, departmentRequest), null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
