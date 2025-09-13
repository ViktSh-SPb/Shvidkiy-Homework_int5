package org.example.employee_service.service;

import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.entity.EmployeeEntity;

public interface EmployeeMapper {
    EmployeeDto entityToDto(EmployeeEntity employeeEntity);

    EmployeeEntity employeeRequestDtoToEntity(EmployeeRequestDto employeeRequestDto);

    void updateEntity(EmployeeEntity employeeEntity, EmployeeRequestDto employeeRequestDto);
}
