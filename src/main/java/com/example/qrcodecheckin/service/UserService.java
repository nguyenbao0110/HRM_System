package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.UserRequest;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.dto.response.UserResponse;
import com.example.qrcodecheckin.entity.Employee;
import com.example.qrcodecheckin.entity.Role;
import com.example.qrcodecheckin.entity.User;
import com.example.qrcodecheckin.enums.RoleEnum;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.UserMapper;
import com.example.qrcodecheckin.repository.EmployeeRepository;
import com.example.qrcodecheckin.repository.RoleRepository;
import com.example.qrcodecheckin.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @CacheEvict(value = "users", allEntries = true)
    @CachePut(value = "users", key = "#result.id")
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        Employee employee = employeeRepository.
                findById(userRequest.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST));

        User user = userMapper.toUser(userRequest);

        user.setEmployee(employee);
        
        Role userRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(Set.of(userRole));

        return userMapper.toResponse(userRepository.save(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Cacheable(value = "users", key = "'page:' + #page + ',size:' + #size")
    public PagedResponse<UserResponse> getAllUsers(int page, int size) {
        Page<User> pageResult = userRepository.findAll(PageRequest.of(page - 1, size));
        return PagedResponse.<UserResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(userMapper::toResponse).toList())
                .build();
    }

}
