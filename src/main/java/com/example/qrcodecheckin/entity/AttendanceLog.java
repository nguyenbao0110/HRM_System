package com.example.qrcodecheckin.entity;

import com.example.qrcodecheckin.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    LocalDateTime checkinTime;

    LocalDateTime checkoutTime;

    @Enumerated(EnumType.STRING)
    AttendanceStatus status;

    Double workHours;
}
