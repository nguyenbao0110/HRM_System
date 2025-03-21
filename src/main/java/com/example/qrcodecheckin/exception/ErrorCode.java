package com.example.qrcodecheckin.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_ERROR(999, "Uncategorized error"),
    INVALID_REQUEST(1000, "Invalid request body"),
    DEPARTMENT_NOT_EXIST(404, "Department not exist"),
    DEPARTMENT_NAME_EXISTED(409, "Department name existed"),
    EMPLOYEE_NOT_EXIST(404, "Employee not exist"),
    EMAIL_EXISTED(409, "Email is existed"),
    USERNAME_EXISTED(409, "Username existed"),
    USERNAME_NOT_EXIST(404, "Username not exist"),
    UNAUTHENTICATED(401, "Unauthenticated"),
    ROLE_NOT_EXIST(404, "Role not exist"),
    GENERATE_TOKEN_FAILED(500, "Failed when generating token"),
    LOCATION_NOT_EXIST(404, "Location not exist"),
    LOCATION_EXISTED(409, "Location already existed"),
    QRCODE_NOT_EXIST(404, "Qr code not exist"),
    INVALID_QRCODE(400, "Qr code is not valid"),
    ALREADY_CHECKED_OUT(400, "Already checked out"),
    SHIFT_NOT_EXIST(404, "Shift not exist"),
    EMPLOYEE_ALREADY_ASSIGNED(409, "Employee already assignment to this shift"),
    INVALID_EMPLOYMENT_TYPE(400, "Can't create assignment for full time employee"),
    NO_SHIFT_ASSIGNED_TODAY(404, "You not have assigned today shift"),
    INVALID_ASSIGNMENT_TIME(400, "Assignment time invalid"),
    NO_VALID_SHIFT_FOR_CHECKIN(400, "You do not have a valid shift to check-in at this time"),
    NO_VALID_SHIFT_FOR_CHECKOUT(400, "You do not have a valid shift to check-out at this time"),
    INVALID_CHECKOUT_DATE(400, "Invalid checkout date"),
    TOKEN_ALREADY_REVOKED(400, "Token already revoked"),
    SHIFT_TIME_CONFLICT(409, "Shift time conflict"),
    SHIFT_ALREADY_PASSED(400, "Shift already passed"),
    ASSIGNMENT_NOT_EXIST(404, "Assignment not exist"),
    INVALID_LOCATION(400, "You must be within 50 meters of the check-in location");

    int code;
    String message;
}
