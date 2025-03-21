package com.example.qrcodecheckin.mapper;

import com.example.qrcodecheckin.dto.request.UserRequest;
import com.example.qrcodecheckin.dto.response.UserResponse;
import com.example.qrcodecheckin.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toResponse(User user);
}
