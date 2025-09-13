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
public class UserRequestDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "John")
    private String name;
    @Email(message = "Некорректный email")
    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(description = "Возраст пользователя", example = "25")
    private Integer age;
}
