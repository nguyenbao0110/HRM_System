package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.AttendanceRequest;
import com.example.qrcodecheckin.dto.response.AttendanceLogResponse;
import com.example.qrcodecheckin.dto.response.AttendanceResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.entity.*;
import com.example.qrcodecheckin.enums.AttendanceStatus;
import com.example.qrcodecheckin.enums.EmploymentType;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.AttendanceLogMapper;
import com.example.qrcodecheckin.mapper.AttendanceMapper;
import com.example.qrcodecheckin.repository.AssignmentRepository;
import com.example.qrcodecheckin.repository.AttendanceLogRepository;
import com.example.qrcodecheckin.repository.QrCodeRepository;
import com.example.qrcodecheckin.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService {
    AttendanceLogRepository attendanceLogRepository;
    UserRepository userRepository;
    QrCodeRepository qrCodeRepository;
    AttendanceMapper attendanceMapper;
    AssignmentRepository assignmentRepository;
    private final AttendanceLogMapper attendanceLogMapper;

    public AttendanceResponse attendance(AttendanceRequest request) {
        User user = getUserFromToken();
        Employee employee = user.getEmployee();
        LocalDateTime now = LocalDateTime.now();

        validateQrCode(request.getQrCodeId(), request.getNonce(), request.getLatitude(), request.getLongitude());

        Optional<AttendanceLog> log = attendanceLogRepository
                .findFirstByEmployeeAndCheckinTimeBetweenOrderByCheckinTimeDesc(
                        employee, now.toLocalDate().atStartOfDay(), now.toLocalDate().atTime(23, 59, 59)
                );

        if (log.isEmpty()) {
            return handleCheckIn(employee, now);
        } else if (log.get().getCheckoutTime() == null) {
            return handleCheckOut(log.get(), now);
        } else {
            throw new AppException(ErrorCode.ALREADY_CHECKED_OUT);
        }
    }

    private AttendanceResponse handleCheckIn(Employee employee, LocalDateTime checkinTime) {
        if (employee.getEmploymentType() != EmploymentType.PART_TIME) {
            throw new AppException(ErrorCode.INVALID_EMPLOYMENT_TYPE);
        }

        List<Assignment> assignments = assignmentRepository.findByEmployeeAndDate(employee, checkinTime.toLocalDate());

        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.NO_SHIFT_ASSIGNED_TODAY);
        }

        Assignment assignment = assignments.stream()
                .filter(a -> checkinTime.toLocalTime().isAfter(a.getShift().getStartTime().minusMinutes(15)))
                .min(Comparator.comparing(a -> a.getShift().getStartTime()))
                .orElseThrow(() -> new AppException(ErrorCode.NO_VALID_SHIFT_FOR_CHECKIN));

        AttendanceStatus status = checkinTime.toLocalTime().isAfter(assignment.getShift().getStartTime())
                ? AttendanceStatus.LATE
                : AttendanceStatus.VALID;

        AttendanceLog log = AttendanceLog.builder()
                .employee(employee)
                .checkinTime(checkinTime)
                .status(status)
                .build();

        return attendanceMapper.toResponse(attendanceLogRepository.save(log));
    }

    private AttendanceResponse handleCheckOut(AttendanceLog log, LocalDateTime checkoutTime) {
        if (!log.getCheckinTime().toLocalDate().isEqual(checkoutTime.toLocalDate())) {
            throw new AppException(ErrorCode.INVALID_CHECKOUT_DATE);
        }

        log.setCheckoutTime(checkoutTime);
        double workHours = Duration.between(log.getCheckinTime(), checkoutTime).toMinutes() / 60.0;
        log.setWorkHours(workHours);

        Employee employee = log.getEmployee();

        if (employee.getEmploymentType() == EmploymentType.PART_TIME) {
            List<Assignment> assignments = assignmentRepository.findByEmployeeAndDate(employee, checkoutTime.toLocalDate());

            if (assignments.isEmpty()) {
                throw new AppException(ErrorCode.NO_SHIFT_ASSIGNED_TODAY);
            }

            Assignment assignment = assignments.stream()
                    .filter(a -> a.getShift().getEndTime().isAfter(checkoutTime.toLocalTime()))
                    .min(Comparator.comparing(a -> a.getShift().getStartTime()))
                    .orElseThrow(() -> new AppException(ErrorCode.NO_VALID_SHIFT_FOR_CHECKOUT));

            double expectedWorkHours = Duration.between(assignment.getShift().getStartTime(), assignment.getShift().getEndTime()).toMinutes() / 60.0;
            double minimumRequiredHours = expectedWorkHours * 0.75;

            if (checkoutTime.toLocalTime().isBefore(assignment.getShift().getEndTime()) || workHours < minimumRequiredHours) {
                log.setStatus(AttendanceStatus.EARLY_CHECKOUT);
            } else {
                log.setStatus(AttendanceStatus.VALID);
            }
        } else {
            throw new AppException(ErrorCode.INVALID_EMPLOYMENT_TYPE);
        }

        return attendanceMapper.toResponse(attendanceLogRepository.save(log));
    }

    public PagedResponse<AttendanceLogResponse> getAttendanceLogs(int page, int size) {
        var pageResult = attendanceLogRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "checkinTime")));
        return PagedResponse.<AttendanceLogResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(attendanceLogMapper::toResponse).toList())
                .build();
    }

    private void validateQrCode(Long qrCodeId, String nonce, double userLat, double userLng) {
        QrCode qrCode = qrCodeRepository.findByIdAndNonce(qrCodeId, nonce)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_QRCODE));

        double distance = calculateDistance(qrCode.getLocation().getLatitude(), qrCode.getLocation().getLongitude(), userLat, userLng);
        if (distance > 50) {
            throw new AppException(ErrorCode.INVALID_LOCATION);
        }
    }

    private User getUserFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXIST));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000;
    }
}
