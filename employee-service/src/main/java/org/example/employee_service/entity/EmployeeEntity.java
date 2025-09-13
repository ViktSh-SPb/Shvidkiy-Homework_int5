package org.example.employee_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Viktor Shvidkiy
 */
@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private Integer age;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
