package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.EmployeeRequest;
import com.example.qrcodecheckin.dto.request.UserRequest;
import com.example.qrcodecheckin.dto.response.EmployeeResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.entity.Department;
import com.example.qrcodecheckin.entity.Employee;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.EmployeeMapper;
import com.example.qrcodecheckin.repository.DepartmentRepository;
import com.example.qrcodecheckin.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeService {
    EmployeeMapper employeeMapper;
    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    private final UserService userService;


    @CacheEvict(value = "employees", allEntries = true)
    @CachePut(value = "employees", key = "#result.id")
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        var department = departmentRepository
                .findById(employeeRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXIST));

        var employee = employeeMapper.toEmployee(employeeRequest);
        employee.setDepartment(department);

        Employee createdEmployee = employeeRepository.save(employee);
        //Default username and password is email of employee
        userService.createUser(
                UserRequest.builder()
                        .firstName(employeeRequest.getFirstName())
                        .lastName(employeeRequest.getLastName())
                        .username(employeeRequest.getEmail())
                        .password(employeeRequest.getEmail())
                        .employeeId(createdEmployee.getId())
                        .build()
        );
        return employeeMapper.toResponse(createdEmployee);
    }

    @Cacheable(value = "employees", key = "#id")
    public EmployeeResponse findEmployeeById(Long id) {
        return employeeMapper.toResponse(
                employeeRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST))
        );
    }

    @Cacheable(value = "employees", key = "'page:' + #page + ',size:' + #size")
    public PagedResponse<EmployeeResponse> findAll(int page, int size) {
        Page<Employee> pageResult = employeeRepository.findAll(PageRequest.of(page - 1, size));
        return PagedResponse.<EmployeeResponse>builder()
                .items(pageResult.getContent().stream().map(employeeMapper::toResponse).toList())
                .totalItems(pageResult.getTotalElements())
                .build();
    }

    @CacheEvict(value = "employees", allEntries = true)
    @CachePut(value = "employees", key = "#id")
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employeeToUpdate = employeeRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST));

        employeeMapper.updateEmployee(employeeToUpdate, employeeRequest);

        if (!Objects.equals(employeeToUpdate.getDepartment().getId(), employeeRequest.getDepartmentId())) {
            Department department = departmentRepository
                    .findById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXIST));

            employeeToUpdate.setDepartment(department);
        }
        employeeRepository.save(employeeToUpdate);
        return employeeMapper.toResponse(employeeToUpdate);
    }

    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmployee(Long id) {
        var employee = employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_EXIST));
        employeeRepository.delete(employee);
    }
}
