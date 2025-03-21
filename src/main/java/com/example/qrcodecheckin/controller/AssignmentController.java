package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.AssignmentRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.AssignmentResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ApiResponse<AssignmentResponse> createAssignment(@RequestBody @Valid AssignmentRequest assignmentRequest) {
        return ApiResponse.success(assignmentService.createAssignment(assignmentRequest), null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<AssignmentResponse>> getAssignments(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(assignmentService.getAssignments(page, size), null);
    }

    @GetMapping("/employee")
    public ApiResponse<List<AssignmentResponse>> getAssignmentsByEmployeeId() {
        return ApiResponse.success(assignmentService.getAssignments(), null);
    }

    @PutMapping("/{id}")
    public ApiResponse<AssignmentResponse> updateAssignment(@PathVariable Long id, @RequestBody @Valid AssignmentRequest assignmentRequest) {
        return ApiResponse.success(assignmentService.updateAssignment(id, assignmentRequest), null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
    }
}
