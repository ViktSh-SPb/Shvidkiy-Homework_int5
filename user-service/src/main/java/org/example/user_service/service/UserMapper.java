package org.example.user_service.service;

import org.example.user_service.dto.UserDto;
import org.example.user_service.dto.UserRequestDto;
import org.example.user_service.entity.UserEntity;

public interface UserMapper {
    UserDto entityToDto(UserEntity userEntity);

    UserEntity userRequestDtoToEntity(UserRequestDto userRequestDto);

    void updateEntity(UserEntity userEntity, UserRequestDto userRequestDto);
}
