package com.example.qrcodecheckin.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentResponse implements Serializable {
    Long id;
    String name;
}
