package org.example.employee_service.service;

import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

/**
 * @author Viktor Shvidkiy
 */
@Component
public class EmployeeMapperImpl implements org.example.employee_service.service.EmployeeMapper {
    public EmployeeDto entityToDto(EmployeeEntity entity){
        return EmployeeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .age(entity.getAge())
                .createdAt(entity.getCreatedAt()!=null? entity.getCreatedAt().toString():null)
                .build();
    }

    public EmployeeEntity employeeRequestDtoToEntity(EmployeeRequestDto dto){
        return EmployeeEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();
    }

    public void updateEntity(EmployeeEntity entity, EmployeeRequestDto dto){
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setAge(dto.getAge());
    }
}
