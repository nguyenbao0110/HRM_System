package com.example.qrcodecheckin.entity;

import com.example.qrcodecheckin.enums.EmploymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String firstName;

    String lastName;

    @Column(nullable = false, unique = true)
    @Email
    String email;

    @Past
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EmploymentType employmentType;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Assignment> assignments;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<AttendanceLog> attendanceLogs;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.REMOVE)
    User user;
}
