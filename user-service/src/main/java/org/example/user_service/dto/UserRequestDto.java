package org.example.user_service.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Viktor Shvidkiy
 */
@Data
@Builder
public class UserRequestDto {
    private String name;
    private String email;
    private Integer age;
}
