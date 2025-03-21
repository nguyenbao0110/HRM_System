package com.example.qrcodecheckin.service;

import com.example.qrcodecheckin.dto.response.PagedResponse;
import com.example.qrcodecheckin.dto.response.ShiftResponse;
import com.example.qrcodecheckin.mapper.ShiftMapper;
import com.example.qrcodecheckin.repository.ShiftRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ShiftService {
    ShiftRepository shiftRepository;
    ShiftMapper shiftMapper;

    public PagedResponse<ShiftResponse> getShifts(int page, int size) {
        var pageResult = shiftRepository.findAll(PageRequest.of(page - 1, size));
        return PagedResponse.<ShiftResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(pageResult.getContent().stream().map(shiftMapper::toResponse).toList())
                .build();
    }
}
