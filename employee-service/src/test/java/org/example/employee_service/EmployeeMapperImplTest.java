package org.example.employee_service;

import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.entity.EmployeeEntity;
import org.example.employee_service.service.EmployeeMapperImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Viktor Shvidkiy
 */
public class EmployeeMapperImplTest {
    private final EmployeeMapperImpl userMapperImpl = new EmployeeMapperImpl();

    @Test
    void testEntityToDto(){
        LocalDateTime time = LocalDateTime.now();

        EmployeeEntity entity = EmployeeEntity.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(time)
                .build();

        EmployeeDto dto = userMapperImpl.entityToDto(entity);

        assertAll(
                ()->assertEquals(entity.getId(), dto.getId()),
                ()->assertEquals(entity.getName(), dto.getName()),
                ()->assertEquals(entity.getEmail(), dto.getEmail()),
                ()->assertEquals(entity.getAge(), dto.getAge()),
                ()->assertEquals(entity.getCreatedAt().toString(), dto.getCreatedAt())

        );
    }

    @Test
    void testRequestDtoToEntity(){
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .build();

        EmployeeEntity entity = userMapperImpl.userRequestDtoToEntity(requestDto);

        assertAll(
                ()->assertNull(entity.getId()),
                ()->assertEquals(entity.getName(), requestDto.getName()),
                ()->assertEquals(entity.getEmail(), requestDto.getEmail()),
                ()->assertEquals(entity.getAge(), requestDto.getAge()),
                ()->assertNull(entity.getCreatedAt())
        );
    }

    @Test
    void testUpdateEntity(){
        EmployeeEntity entity = EmployeeEntity.builder()
                .id(1)
                .name("TomBefore")
                .email("before@gmail.com")
                .age(19)
                .build();

        EmployeeRequestDto updateDto = EmployeeRequestDto.builder()
                .name("TomAfter")
                .email("after@gmail.com")
                .age(20)
                .build();

        userMapperImpl.updateEntity(entity, updateDto);

        assertAll(
                ()->assertEquals("TomAfter", entity.getName()),
                ()->assertEquals("after@gmail.com", entity.getEmail()),
                ()->assertEquals(20, entity.getAge())
        );
    }
}
