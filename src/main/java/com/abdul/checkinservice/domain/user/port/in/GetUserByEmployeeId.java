package com.abdul.checkinservice.domain.user.port.in;

import com.abdul.checkinservice.domain.user.model.UserDto;

public interface GetUserByEmployeeId {
    UserDto execute(Long employeeId);
}
