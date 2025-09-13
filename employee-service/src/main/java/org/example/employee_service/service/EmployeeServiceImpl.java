package org.example.employee_service.service;

import org.example.employee_service.exception.EmployeeNotFoundException;
import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.entity.EmployeeEntity;
import org.example.employee_service.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Viktor Shvidkiy
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeRequestDto dto) {
        EmployeeEntity employee = employeeMapper.employeeRequestDtoToEntity(dto);
        employee.setCreatedAt(LocalDateTime.now());
        EmployeeEntity saved = employeeRepository.save(employee);
        return employeeMapper.entityToDto(saved);
    }

    @Transactional
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::entityToDto)
                .toList();
    }

    @Transactional
    public EmployeeDto getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::entityToDto)
                .orElseThrow(() -> new EmployeeNotFoundException("Пользователь с ID: " + id + " не найден."));
    }

    @Transactional
    public EmployeeDto updateEmployee(Integer id, EmployeeRequestDto dto) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employeeMapper.updateEntity(employee, dto);
                    return employeeMapper.entityToDto(employeeRepository.save(employee));
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Пользователь с ID: " + id + " не найден."));
    }

    @Transactional
    public void deleteEmployee(Integer id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                        .orElseThrow(()->new EmployeeNotFoundException("Пользователь с ID: " + id + " не найден."));

        employeeRepository.deleteById(id);
    }
}
