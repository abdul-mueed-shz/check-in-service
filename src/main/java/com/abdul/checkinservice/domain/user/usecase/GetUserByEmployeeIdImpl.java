package com.abdul.checkinservice.domain.user.usecase;

import com.abdul.checkinservice.domain.user.model.UserDto;
import com.abdul.checkinservice.domain.user.port.in.GetUserByEmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByEmployeeIdImpl implements GetUserByEmployeeId {
    @Override
    public UserDto execute(Long employeeId) {
        return UserDto.builder()
                .id(employeeId)
                .email("abdulmueedshahbaz@gmail.com")
                .displayName("Abdul Mueed Shahbaz")
                .build();
    }
}
