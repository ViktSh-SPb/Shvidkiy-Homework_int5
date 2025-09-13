package org.example.employee_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.employee_service.controller.EmployeeController;
import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.exception.EmployeeNotFoundException;
import org.example.employee_service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Viktor Shvidkiy
 */
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void createUserSuccess() throws Exception{
        String time = LocalDateTime.now().toString();
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .build();
        EmployeeDto createdDto = EmployeeDto.builder()
                .id(1)
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .createdAt(time)
                .build();

        when(employeeService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Jack"))
                .andExpect(jsonPath("$.email").value("jack@gmail.com"))
                .andExpect(jsonPath("$.age").value("20"))
                .andExpect(jsonPath("$.createdAt").value(time));
    }

    @Test
    void getAllUsersShouldReturnList() throws Exception {
        String time1 = LocalDateTime.now().minusMinutes(10).toString();
        String time2 = LocalDateTime.now().toString();
        List<EmployeeDto> users = List.of(
                EmployeeDto.builder()
                        .id(1)
                        .name("Jack")
                        .email("jack@gmail.com")
                        .age(30)
                        .createdAt(time1)
                        .build(),
                EmployeeDto.builder()
                        .id(2)
                        .name("Bill")
                        .email("bill@gmail.com")
                        .age(35)
                        .createdAt(time2)
                        .build()
        );

        when(employeeService.getAllEmployees()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Jack"))
                .andExpect(jsonPath("$[0].email").value("jack@gmail.com"))
                .andExpect(jsonPath("$[0].age").value("30"))
                .andExpect(jsonPath("$[0].createdAt").value(time1))
                .andExpect(jsonPath("$[1].name").value("Bill"))
                .andExpect(jsonPath("$[1].email").value("bill@gmail.com"))
                .andExpect(jsonPath("$[1].age").value("35"))
                .andExpect(jsonPath("$[1].createdAt").value(time2));
    }

    @Test
    void getUserByIdFound() throws Exception {
        String time = LocalDateTime.now().toString();
        EmployeeDto dto = EmployeeDto.builder()
                .id(1)
                .name("Bob")
                .email("bob@gmail.com")
                .age(40)
                .createdAt(time)
                .build();

        when(employeeService.getEmployeeById(1)).thenReturn(dto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@gmail.com"))
                .andExpect(jsonPath("$.age").value("40"))
                .andExpect(jsonPath("$.createdAt").value(time));
    }

    @Test
    void getUserByIdNotFound() throws Exception{
        int userId = 100;
        when(employeeService.getEmployeeById(userId))
                .thenThrow(new EmployeeNotFoundException("Пользователь с ID: " + userId + " не найден."));

        mockMvc.perform(get("/users/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID: 100 не найден."));
    }

    @Test
    void updateUserSuccess() throws Exception{
        String time = LocalDateTime.now().toString();
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Tom")
                .email("tom@gmail.com")
                .age(20)
                .build();
        EmployeeDto updatedDto = EmployeeDto.builder()
                .id(1)
                .name("Tom")
                .email("tom@gmail.com")
                .age(20)
                .createdAt(time)
                .build();

        when(employeeService.updateEmployee(eq(1), any(EmployeeRequestDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.email").value("tom@gmail.com"))
                .andExpect(jsonPath("$.age").value("20"))
                .andExpect(jsonPath("$.createdAt").value(time));
    }

    @Test
    void updateUserNotFound() throws Exception{
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Max")
                .email("max@gmail.com")
                .age(25)
                .build();

        when(employeeService.updateEmployee(eq(200), any(EmployeeRequestDto.class)))
                .thenThrow(new EmployeeNotFoundException("Пользователь с ID: 200 не найден."));

        mockMvc.perform(put("/users/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserSuccess() throws Exception{
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(employeeService).deleteEmployee(1);
    }
}
