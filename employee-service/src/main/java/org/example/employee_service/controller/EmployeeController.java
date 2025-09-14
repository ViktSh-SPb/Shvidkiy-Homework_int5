package org.example.employee_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.employee_service.service.EmployeeService;
import org.example.employee_service.dto.EmployeeDto;
import org.example.employee_service.dto.EmployeeRequestDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Viktor Shvidkiy
 */
@Tag(name = "Employees", description = "Операции с сотрудниками")
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Создать нового сотрудника")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Сотрудник успешно создан",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(
                            type = "object",
                            additionalPropertiesSchema = String.class,
                            example = "{\"name\":\"Имя не может быть пустым\", \"email\":\"Некорректный email\", \"age\":\"Возраст не может быть отрицательным\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PostMapping
    public EntityModel<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeDto createdEmployee = employeeService.createEmployee(dto);
        return toEntityModel(createdEmployee);
    }

    @Operation(summary = "Получить список всех сотрудников")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список сотрудников получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeDto.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping
    public CollectionModel<EntityModel<EmployeeDto>> getAllEmployees() {
        List<EntityModel<EmployeeDto>> employees = employeeService.getAllEmployees()
                .stream()
                .map(this::toEntityModel)
                .toList();
        return CollectionModel.of(employees, WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmployeeController.class)
                        .getAllEmployees())
                .withSelfRel()
        );
    }

    @Operation(summary = "Найти сотрудника по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Сотрудник найден",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сотрудник не найден",
                    content = @Content(schema = @Schema(
                            type = "object",
                            additionalPropertiesSchema = String.class,
                            example = "{\"name\":\"Имя не может быть пустым\", \"email\":\"Некорректный email\", \"age\":\"Возраст не может быть отрицательным\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public EntityModel<EmployeeDto> getEmployeeById(@PathVariable Integer id) {
        return toEntityModel(employeeService.getEmployeeById(id));
    }

    @Operation(summary = "Обновить данные сотрудника")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные успешно обновлены",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public EntityModel<EmployeeDto> updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeRequestDto dto) {
        return toEntityModel(employeeService.updateEmployee(id, dto));
    }

    @Operation(summary = "Удалить сотрудника")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сотрудник удален"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<EmployeeDto> toEntityModel(EmployeeDto employee){
        return EntityModel.of(employee,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                        .getEmployeeById(employee.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                        .updateEmployee(employee.getId(), null)).withRel("update"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                        .deleteEmployee(employee.getId())).withRel("delete"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                        .getAllEmployees()).withRel("all-employees")
        );
    }
}
