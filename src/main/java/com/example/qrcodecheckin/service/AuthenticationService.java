package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.AuthenticationRequest;
import com.example.qrcodecheckin.dto.request.LogoutRequest;
import com.example.qrcodecheckin.dto.request.RefreshRequest;
import com.example.qrcodecheckin.dto.response.AuthenticationResponse;
import com.example.qrcodecheckin.entity.RevokedToken;
import com.example.qrcodecheckin.entity.Role;
import com.example.qrcodecheckin.entity.User;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.repository.RevokedTokenRepository;
import com.example.qrcodecheckin.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RevokedTokenRepository revokedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    Long REFRESHABLE_DURATION;


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository
                .findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXIST));

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return AuthenticationResponse
                .builder()
                .accessToken(generateToken(user))
                .build();
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        var signedToken = verifyToken(logoutRequest.getToken());

        RevokedToken revokedToken = RevokedToken.builder()
                .id(signedToken.getJWTClaimsSet().getJWTID())
                .expiryTime(signedToken.getJWTClaimsSet().getExpirationTime())
                .build();

        revokedTokenRepository.save(revokedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken());

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        RevokedToken invalidatedToken = RevokedToken
                .builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        revokedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse
                .builder()
                .accessToken(token)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli());

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (revokedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .issuer("thuatphan.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate token");
            throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
        }
    }

    private String buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if (user.getRoles() != null) {
            List<Role> rolesCopy = new ArrayList<>(user.getRoles());
            for (Role role : rolesCopy) {
                if (role.getName() != null) {
                    scopeJoiner.add(role.getName().name());
                }
            }
        }
        return scopeJoiner.toString();
    }
}
