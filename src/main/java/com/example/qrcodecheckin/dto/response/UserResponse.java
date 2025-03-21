package com.example.qrcodecheckin.dto.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse implements Serializable {
    Long id;
    String username;
    String firstName;
    String lastName;
}
