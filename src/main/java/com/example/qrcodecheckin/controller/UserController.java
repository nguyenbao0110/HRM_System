package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.UserRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.dto.response.UserResponse;
import com.example.qrcodecheckin.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return ApiResponse.success(createdUser, null);
    }

    @GetMapping
    public ApiResponse<PagedResponse<UserResponse>> getAllUsers(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.success(userService.getAllUsers(page, size), null);
    }
}
