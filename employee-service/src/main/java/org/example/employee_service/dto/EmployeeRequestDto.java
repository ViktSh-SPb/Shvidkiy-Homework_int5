package org.example.employee_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * @author Viktor Shvidkiy
 */
@Data
@Builder
public class EmployeeRequestDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "Имя сотрудника", example = "John")
    private String name;
    @Schema(description = "Должность сотрудника", example = "manager")
    private String jobTitle;
    @Email(message = "Некорректный email")
    @Schema(description = "Email сотрудника", example = "john@example.com")
    private String email;
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(description = "Возраст сотрудника", example = "25")
    private Integer age;
}
