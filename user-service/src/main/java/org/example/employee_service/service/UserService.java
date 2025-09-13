package org.example.employee_service.service;

import org.example.employee_service.dto.UserDto;
import org.example.employee_service.dto.UserRequestDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRequestDto dto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Integer id);
    UserDto updateUser(Integer id, UserRequestDto dto);
    void deleteUser(Integer id);
}
