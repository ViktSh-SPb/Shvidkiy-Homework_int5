package org.example.employee_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Viktor Shvidkiy
 */
@Data
@Builder
public class EmployeeDto {
    @Schema(description = "ID сотрудника", example = "1")
    private Integer id;
    @Schema(description = "Имя сотрудника", example = "John")
    private String name;
    @Schema(description = "Должность сотрудника", example = "manager")
    private String jobTitle;
    @Schema(description = "Email сотрудника", example = "john@example.com")
    private String email;
    @Schema(description = "Возраст сотрудника", example = "25")
    private Integer age;
    @Schema(description = "Дата внесения сотрудника", example = "2025-09-01 02:46:09.910872")
    private String createdAt;
}
