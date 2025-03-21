package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.AssignmentRequest;
import com.example.qrcodecheckin.dto.response.AssignmentResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.entity.Assignment;
import com.example.qrcodecheckin.entity.Shift;
import com.example.qrcodecheckin.entity.User;
import com.example.qrcodecheckin.enums.EmploymentType;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.AssignmentMapper;
import com.example.qrcodecheckin.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentService {
    EmployeeRepository employeeRepository;
    ShiftRepository shiftRepository;
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public AssignmentResponse createAssignment(AssignmentRequest assignmentRequest) {
        var assignment = validateAndPrepareAssignment(null, assignmentRequest);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    public AssignmentResponse updateAssignment(long assignmentId, AssignmentRequest assignmentRequest) {
        var existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_EXIST));

        var updatedAssignment = validateAndPrepareAssignment(existingAssignment, assignmentRequest);
        return assignmentMapper.toResponse(assignmentRepository.save(updatedAssignment));
    }

    private Assignment validateAndPrepareAssignment(Assignment existingAssignment, AssignmentRequest assignmentRequest) {
        var employee = employeeRepository.findById(assignmentRequest.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST));

        if (employee.getEmploymentType().equals(EmploymentType.FULL_TIME)) {
            throw new AppException(ErrorCode.INVALID_EMPLOYMENT_TYPE);
        }

        var shift = shiftRepository.findById(assignmentRequest.getShiftId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIFT_NOT_EXIST));

        var location = locationRepository.findById(assignmentRequest.getLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_EXIST));

        LocalDate requestDate = assignmentRequest.getDate();

        if (isPastShift(requestDate, shift)) {
            throw new AppException(ErrorCode.SHIFT_ALREADY_PASSED);
        }

        if (existingAssignment == null || !existingAssignment.getShift().getId().equals(shift.getId())) {
            boolean isTimeOverlapping = assignmentRepository.findByEmployeeAndDate(employee, requestDate)
                    .stream()
                    .anyMatch(existing -> isShiftOverlapping(existing.getShift(), shift));

            if (isTimeOverlapping) {
                throw new AppException(ErrorCode.SHIFT_TIME_CONFLICT);
            }
        }

        return Assignment.builder()
                .employee(employee)
                .shift(shift)
                .location(location)
                .date(requestDate)
                .build();
    }

    public PagedResponse<AssignmentResponse> getAssignments(int page, int size) {
        Page<Assignment> pageResult = assignmentRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "date"))
        );
        return PagedResponse.<AssignmentResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(assignmentMapper::toResponse).toList())
                .build();
    }

    public List<AssignmentResponse> getAssignments() {
        var user = getUserFromToken();
        var employee = employeeRepository
                .findById(user.getEmployee().getId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST));

        if (employee.getEmploymentType().equals(EmploymentType.FULL_TIME)) {
            throw new AppException(ErrorCode.INVALID_EMPLOYMENT_TYPE);
        }

        return assignmentRepository
                .findByEmployee(employee)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }

    public void deleteAssignment(long assignmentId) {
        var assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_EXIST));
        assignmentRepository.delete(assignment);
    }

    private boolean isPastShift(LocalDate date, Shift shift) {
        LocalDate today = LocalDate.now();
        return date.isBefore(today) || (date.isEqual(today) && shift.getEndTime().isBefore(LocalTime.now()));
    }

    private boolean isShiftOverlapping(Shift existingShift, Shift newShift) {
        return !(newShift.getEndTime().isBefore(existingShift.getStartTime()) ||
                newShift.getStartTime().isAfter(existingShift.getEndTime()));
    }

    private User getUserFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXIST));
    }
}

