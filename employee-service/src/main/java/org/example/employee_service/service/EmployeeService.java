package org.example.employee_service.service;

import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeRequestDto dto);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(Integer id);
    EmployeeDto updateEmployee(Integer id, EmployeeRequestDto dto);
    void deleteEmployee(Integer id);
}
