package org.example.employee_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.example.employee_service.repository.EmployeeRepository;
import org.example.employee_service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Viktor Shvidkiy
 */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class EmployeeControllerIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("test_db")
            .withUsername("user")
            .withPassword("user");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.employeename", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", ()->"create-drop");
    }

    @Test
    void createEmployeeSuccess() throws Exception{
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(20)
                .build();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Jack"))
                .andExpect(jsonPath("$.email").value("jack@gmail.com"))
                .andExpect(jsonPath("$.age").value("20"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void getAllEmployeesShouldReturnList() throws Exception {
        EmployeeDto employee1 = employeeService.createEmployee(EmployeeRequestDto.builder()
                        .name("Jack")
                        .email("jack@gmail.com")
                        .age(30)
                .build());
        EmployeeDto employee2 = employeeService.createEmployee(EmployeeRequestDto.builder()
                .name("Bill")
                .email("bill@gmail.com")
                .age(35)
                .build());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(employee1.getId()))
                .andExpect(jsonPath("$[0].name").value(employee1.getName()))
                .andExpect(jsonPath("$[0].email").value(employee1.getEmail()))
                .andExpect(jsonPath("$[0].age").value(employee1.getAge()))
                .andExpect(jsonPath("$[0].createdAt").value(employee1.getCreatedAt()))
                .andExpect(jsonPath("$[1].id").value(employee2.getId()))
                .andExpect(jsonPath("$[1].name").value(employee2.getName()))
                .andExpect(jsonPath("$[1].email").value(employee2.getEmail()))
                .andExpect(jsonPath("$[1].age").value(employee2.getAge()))
                .andExpect(jsonPath("$[1].createdAt").value(employee2.getCreatedAt()));
    }

    @Test
    void getEmployeeByIdFound() throws Exception {
        EmployeeDto employee = employeeService.createEmployee(EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(30)
                .build());

        mockMvc.perform(get("/employees/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.email").value(employee.getEmail()))
                .andExpect(jsonPath("$.age").value(employee.getAge()))
                .andExpect(jsonPath("$.createdAt").value(employee.getCreatedAt()));
    }

    @Test
    void getEmployeeByIdNotFound() throws Exception{
        int employeeId = 100;

        mockMvc.perform(get("/employees/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID: 100 не найден."));
    }

    @Test
    void updateEmployeeSuccess() throws Exception{
        EmployeeDto employee = employeeService.createEmployee(EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(30)
                .build());

        EmployeeRequestDto updatedEmployee = EmployeeRequestDto.builder()
                .name("MrJack")
                .email("ceo_jack@gmail.com")
                .age(31)
                .build();

        mockMvc.perform(put("/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.createdAt").value(employee.getCreatedAt()));
    }

    @Test
    void updateEmployeeNotFound() throws Exception{
        EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                .name("Max")
                .email("max@gmail.com")
                .age(25)
                .build();

        mockMvc.perform(put("/employees/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь с ID: 200 не найден."));
    }

    @Test
    void deleteEmployeeSuccess() throws Exception{
        EmployeeDto employee = employeeService.createEmployee(EmployeeRequestDto.builder()
                .name("Jack")
                .email("jack@gmail.com")
                .age(30)
                .build());

        mockMvc.perform(delete("/employees/{id}", employee.getId()))
                .andExpect(status().isNoContent());
    }
}
