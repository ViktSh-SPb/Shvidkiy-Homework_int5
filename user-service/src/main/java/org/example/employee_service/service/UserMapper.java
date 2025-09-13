package org.example.employee_service.service;

import org.example.employee_service.dto.UserDto;
import org.example.employee_service.dto.UserRequestDto;
import org.example.employee_service.entity.UserEntity;

public interface UserMapper {
    UserDto entityToDto(UserEntity userEntity);

    UserEntity userRequestDtoToEntity(UserRequestDto userRequestDto);

    void updateEntity(UserEntity userEntity, UserRequestDto userRequestDto);
}
