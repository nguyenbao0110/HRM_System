package com.example.qrcodecheckin.repository;

import com.example.qrcodecheckin.entity.AttendanceLog;
import com.example.qrcodecheckin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    Optional<AttendanceLog> findFirstByEmployeeAndCheckinTimeBetweenOrderByCheckinTimeDesc(Employee employee, LocalDateTime start, LocalDateTime end);
}
