package org.example.employee_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Viktor Shvidkiy
 */
@Data
@Builder
public class UserDto {
    @Schema(description = "ID пользователя", example = "1")
    private Integer id;
    @Schema(description = "Имя пользователя", example = "John")
    private String name;
    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;
    @Schema(description = "Возраст пользователя", example = "25")
    private Integer age;
    @Schema(description = "Дата внесения пользователя", example = "2025-09-01 02:46:09.910872")
    private String createdAt;
}
