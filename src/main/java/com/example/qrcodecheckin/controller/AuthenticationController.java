package com.example.qrcodecheckin.controller;

import com.example.qrcodecheckin.dto.request.AuthenticationRequest;
import com.example.qrcodecheckin.dto.request.LogoutRequest;
import com.example.qrcodecheckin.dto.request.RefreshRequest;
import com.example.qrcodecheckin.dto.response.ApiResponse;
import com.example.qrcodecheckin.dto.response.AuthenticationResponse;
import com.example.qrcodecheckin.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ApiResponse.success(authenticationService.authenticate(authenticationRequest), null);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.success(null, "Logout successful");
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshRequest refreshRequest) throws ParseException, JOSEException {
        return ApiResponse.success(authenticationService.refreshToken(refreshRequest), null);
    }
}
