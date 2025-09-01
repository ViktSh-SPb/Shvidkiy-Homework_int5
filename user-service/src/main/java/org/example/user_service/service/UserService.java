package org.example.user_service.service;

import org.example.user_service.dto.UserDto;
import org.example.user_service.dto.UserRequestDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRequestDto dto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Integer id);
    UserDto updateUser(Integer id, UserRequestDto dto);
    void deleteUser(Integer id);
}
