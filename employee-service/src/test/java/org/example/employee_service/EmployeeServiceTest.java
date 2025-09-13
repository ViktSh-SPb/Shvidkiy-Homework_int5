package org.example.employee_service;

import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.entity.EmployeeEntity;
import org.example.employee_service.exception.EmployeeNotFoundException;
import org.example.employee_service.repository.EmployeeRepository;
import org.example.employee_service.service.EmployeeMapper;
import org.example.employee_service.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Viktor Shvidkiy
 */
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity employeeEntity;
    private EmployeeDto employeeDto;
    private EmployeeRequestDto employeeRequestDto;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        employeeEntity = EmployeeEntity.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(LocalDateTime.now())
                .build();

        employeeDto = EmployeeDto.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(employeeEntity.getCreatedAt().toString())
                .build();

        employeeRequestDto = EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .build();
    }

    @Test
    void testCreateEmployee(){
        when(employeeMapper.employeeRequestDtoToEntity(employeeRequestDto)).thenReturn(employeeEntity);
        when(employeeRepository.save(employeeEntity)).thenReturn(employeeEntity);
        when(employeeMapper.entityToDto(employeeEntity)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.createEmployee(employeeRequestDto);

        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(employeeDto.getId(), result.getId())
        );
        verify(employeeRepository).save(employeeEntity);
    }

    @Test
    void testGetAllEmployees(){
        List<EmployeeEntity> employees = Arrays.asList(employeeEntity);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.entityToDto(employeeEntity)).thenReturn(employeeDto);

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertAll(
                ()->assertEquals(1, result.size()),
                ()->assertEquals(employeeDto.getId(), result.get(0).getId())
        );
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetAllEmployeesEmpty(){
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertAll(
                ()->assertNotNull(result),
                ()->assertTrue(result.isEmpty())
        );
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetEmployeeByIdFound(){
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.entityToDto(employeeEntity)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.getEmployeeById(1);

        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(employeeDto.getId(), result.getId())
        );
        verify(employeeRepository).findById(1);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1)
        );

        assertEquals("Пользователь с ID: 1 не найден.", exception.getMessage());
        verify(employeeRepository).findById(1);
    }

    @Test
    void testUpdateEmployeeFound(){
        EmployeeRequestDto updateDto = EmployeeRequestDto.builder()
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .build();

        EmployeeEntity updatedEntity = EmployeeEntity.builder()
                .id(1)
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .createdAt(employeeEntity.getCreatedAt())
                .build();

        EmployeeDto updatedDto = EmployeeDto.builder()
                .id(1)
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(21)
                .createdAt(updatedEntity.getCreatedAt().toString())
                .build();

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
        doAnswer(invocation -> {
            EmployeeEntity entity = invocation.getArgument(0);
            entity.setName(updateDto.getName());
            entity.setEmail(updateDto.getEmail());
            entity.setAge(updateDto.getAge());
            return null;
        }).when(employeeMapper).updateEntity(employeeEntity, updateDto);
        when(employeeRepository.save(employeeEntity)).thenReturn(updatedEntity);
        when(employeeMapper.entityToDto(updatedEntity)).thenReturn(updatedDto);

        EmployeeDto result = employeeService.updateEmployee(1,updateDto);

        assertAll(
                ()->assertEquals("MrJack", result.getName()),
                ()->assertEquals("ceo_jack@gmail.com", result.getEmail()),
                ()->assertEquals(21,result.getAge())
        );
        verify(employeeRepository).findById(1);
        verify(employeeRepository).save(employeeEntity);
    }

    @Test
    void testUpdateEmployeeNotFound(){
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            employeeService.updateEmployee(1, employeeRequestDto);
        });
        assertEquals("Пользователь с ID: 1 не найден.", exception.getMessage());
        verify(employeeRepository).findById(1);
    }

    @Test
    void testDeleteEmployee(){
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
        doNothing().when(employeeRepository).deleteById(1);
        employeeService.deleteEmployee(1);
        verify(employeeRepository).deleteById(1);
    }
}