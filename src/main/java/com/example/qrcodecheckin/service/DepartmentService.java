package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.request.DepartmentRequest;
import com.example.qrcodecheckin.dto.response.DepartmentResponse;
import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.entity.Department;
import com.example.qrcodecheckin.exception.AppException;
import com.example.qrcodecheckin.exception.ErrorCode;
import com.example.qrcodecheckin.mapper.DepartmentMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.qrcodecheckin.repository.DepartmentRepository;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DepartmentService {
    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @CacheEvict(value = "departments", allEntries = true)
    @CachePut(value = "departments", key = "#result.id")
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.getName())) {
            throw new AppException(ErrorCode.DEPARTMENT_NAME_EXISTED);
        }
        Department createdDepartment = departmentRepository.save(departmentMapper.toDepartment(departmentRequest));
        return departmentMapper.toResponse(createdDepartment);
    }

    @Cacheable(value = "departments", key = "#id")
    public DepartmentResponse findDepartmentById(Long id) {
        return departmentMapper.toResponse(departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXIST)));
    }

    @Cacheable(value = "departments", key = "'page:' + #page + ',size:' + #size")
    public PagedResponse<DepartmentResponse> findAll(int page, int size) {
        Page<Department> pageResult = departmentRepository.findAll(PageRequest.of(page - 1, size));
        return PagedResponse.<DepartmentResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(departmentMapper::toResponse).toList())
                .build();
    }

    @CacheEvict(value = "departments", allEntries = true)
    @CachePut(value = "departments", key = "#id")
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) {
        Department departmentToUpdate = departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXIST));
        departmentMapper.updateDepartment(departmentToUpdate, departmentRequest);
        return departmentMapper.toResponse(departmentRepository.save(departmentToUpdate));
    }

    @CacheEvict(value = "departments", allEntries = true)
    public void deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return;
        }
        throw new AppException(ErrorCode.DEPARTMENT_NOT_EXIST);
    }
}
